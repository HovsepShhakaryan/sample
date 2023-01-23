package com.vylo.main.responsemain.createvideo.presentation.fragments

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Consumer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.viewpager2.widget.ViewPager2
import androidx.work.await
import com.hovsep.camera.createvideo.presentation.extensions.getAspectRatio
import com.vylo.common.BaseFragment
import com.vylo.common.util.TimerService
import com.vylo.common.util.enums.UploadFileType
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.component.sharedviewmodel.NavigationSharedViewModel
import com.vylo.main.databinding.FragmentCaptureBinding
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.component.getScopeName
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Handler
import kotlin.concurrent.schedule
import kotlin.math.roundToInt


private var PERMISSIONS_REQUIRED = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

class CaptureFragment(
    private val pager: ViewPager2
) : BaseFragment<FragmentCaptureBinding>() {

    private val sharedViewModel by sharedViewModel<NavigationSharedViewModel>()
    override fun getViewBinding() = FragmentCaptureBinding.inflate(layoutInflater)

    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0
    private var isClickedStopButton = false
    private lateinit var activity: MainFlawActivity

    enum class UiState {
        IDLE,       // Not recording, all UI controls are active.
        RECORDING,  // Camera is recording, only display Pause/Resume & Stop button.
        FINALIZED,  // Recording just completes, disable all RECORDING UI controls.
    }

    private val captureViewBinding get() = viewBinder
    private val captureLiveStatus = MutableLiveData<String>()
    private val cameraCapabilities = mutableListOf<CameraCapability>()
    private lateinit var videoCapture: VideoCapture<Recorder>
    private var currentRecording: Recording? = null
    private lateinit var recordingState: VideoRecordEvent
    private var cameraIndex = 1
    private var qualityIndex = DEFAULT_QUALITY_IDX
    private var audioEnabled = true
    private val mainThreadExecutor by lazy { ContextCompat.getMainExecutor(requireContext()) }
    private var enumerationDeferred: Deferred<Unit>? = null

    companion object {
        const val DEFAULT_QUALITY_IDX = 0
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionList = PERMISSIONS_REQUIRED.toMutableList()
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        PERMISSIONS_REQUIRED = permissionList.toTypedArray()

        if (!hasPermissions(requireContext()))
            activityResultLauncher.launch(PERMISSIONS_REQUIRED)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = FragmentCaptureBinding.inflate(inflater, container, false)
        return captureViewBinding.root
    }

    override fun onPause() {
        super.onPause()
        startStopTimer()
        resetTimer()
        viewBinder.navBar.visibility = View.VISIBLE
        viewBinder.recordTimeView.visibility = View.GONE
        isClickedStopButton = false
    }

    override fun onResume() {
        super.onResume()
        initCameraFragment()
        serviceIntent = Intent(requireContext(), TimerService::class.java)
        requireContext().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainFlawActivity
    }

    private fun createNavigationBar() {
        viewBinder.navBar.apply {
            val typeface = ResourcesCompat.getFont(requireContext(), com.vylo.common.R.font.suisse_lntl_medium)
            setTitleFontFamily(typeface)
            setTitle(resources.getString(com.vylo.common.R.string.title_record_video))
            setTitleStyle(com.vylo.common.R.style.MainText_H4)
        }
    }

    private suspend fun bindCaptureUseCase() {
        val cameraProvider = ProcessCameraProvider.getInstance(requireContext()).await()
        val cameraSelector = getCameraSelector(cameraIndex)
        if (cameraCapabilities[cameraIndex].qualities.isNotEmpty()) {
            val quality = cameraCapabilities[cameraIndex].qualities[qualityIndex]
            val qualitySelector = QualitySelector.from(quality)

            val preview = Preview.Builder()
                .setTargetAspectRatio(quality.getAspectRatio(quality))
                .build().apply {
                    setSurfaceProvider(captureViewBinding.previewView.surfaceProvider)
                }

            val recorder = Recorder.Builder()
                .setQualitySelector(qualitySelector)
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    videoCapture,
                    preview
                )

                val cameraControl: CameraControl = camera.cameraControl
                var isFlashTurnedOn = false
                viewBinder.flashButton.setImageResource(com.vylo.main.R.drawable.ic_flash_off)
                viewBinder.flashButton.setOnClickListener {
                    when (isFlashTurnedOn) {
                        false -> {
                            isFlashTurnedOn = true
                            cameraControl.enableTorch(isFlashTurnedOn)
                            viewBinder.flashButton.setImageResource(com.vylo.main.R.drawable.ic_flash)
                        }
                        true -> {
                            isFlashTurnedOn = false
                            cameraControl.enableTorch(isFlashTurnedOn)
                            viewBinder.flashButton.setImageResource(com.vylo.main.R.drawable.ic_flash_off)
                        }
                    }
                }

            } catch (exc: Exception) {
                resetUIandState("bindToLifecycle failed: $exc")
            }
            enableUI(true)
        }
    }

    private fun startRecording() {
        val name = "CameraX-recording-" +
                SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                    .format(System.currentTimeMillis()) + ".mp4"

        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
        }

        val mediaStoreOutput = MediaStoreOutputOptions.Builder(
            requireActivity().contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
            .setContentValues(contentValues)
            .build()

        currentRecording = videoCapture.output
            .prepareRecording(requireActivity(), mediaStoreOutput)
            .apply {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED
                )
                    return
                if (audioEnabled) withAudioEnabled()
            }.start(mainThreadExecutor, captureListener)
    }


    private val captureListener = Consumer<VideoRecordEvent> { event ->
        if (event !is VideoRecordEvent.Status)
            recordingState = event

        updateUI(event)

        if (event is VideoRecordEvent.Finalize) {
            lifecycleScope.launch {
                if (isClickedStopButton) {
                    activity.showProgress()
                    sharedViewModel.setUri(event.outputResults.outputUri, UploadFileType.VIDEO)
                    Timer().schedule(1000) {
                        requireActivity().runOnUiThread(Runnable {
                            pager.currentItem = 1
                        })
                    }
                }
            }
        }
    }

    private fun getCameraSelector(idx: Int): CameraSelector {
        if (cameraCapabilities.size == 0) requireActivity().finish()
        return (cameraCapabilities[idx % cameraCapabilities.size].camSelector)
    }

    data class CameraCapability(val camSelector: CameraSelector, val qualities: List<Quality>)

    init {
        enumerationDeferred = lifecycleScope.async {
            whenCreated {
                val provider = ProcessCameraProvider.getInstance(requireContext()).await()

                provider.unbindAll()
                for (camSelector in arrayOf(
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    CameraSelector.DEFAULT_FRONT_CAMERA
                )) {
                    try {
                        if (provider.hasCamera(camSelector)) {
                            val camera = provider.bindToLifecycle(requireActivity(), camSelector)
                            QualitySelector
                                .getSupportedQualities(camera.cameraInfo)
                                .filter { quality ->
                                    listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD)
                                        .contains(quality)
                                }.also {
                                    cameraCapabilities.add(CameraCapability(camSelector, it))
                                }
                        }
                    } catch (exc: java.lang.Exception) {
                        showMessage("Camera Face $camSelector is not supported")
                    }
                }
            }
        }
    }

    private fun initCameraFragment() {
        initializeUI()
        createNavigationBar()
        viewLifecycleOwner.lifecycleScope.launch {
            if (enumerationDeferred != null) {
                enumerationDeferred!!.await()
                enumerationDeferred = null
            }
            bindCaptureUseCase()
        }
    }

    private fun initializeUI() {
        captureViewBinding.cameraButton.apply {
            setOnClickListener {
                cameraIndex = (cameraIndex + 1) % cameraCapabilities.size
                qualityIndex = DEFAULT_QUALITY_IDX
                enableUI(false)
                viewLifecycleOwner.lifecycleScope.launch { bindCaptureUseCase() }
            }
            isEnabled = false
        }


        captureViewBinding.captureButton.apply {
            setOnClickListener {
                resetTimer()
                startStopTimer()
                if (!this@CaptureFragment::recordingState.isInitialized ||
                    recordingState is VideoRecordEvent.Finalize
                ) {
                    enableUI(false)
                    startRecording()
                } else {
                    when (recordingState) {
                        is VideoRecordEvent.Start -> {
                            currentRecording?.pause()
                            captureViewBinding.stopButton.visibility = View.VISIBLE
                        }
                        is VideoRecordEvent.Pause -> currentRecording?.resume()
                        is VideoRecordEvent.Resume -> currentRecording?.pause()
                        else -> throw IllegalStateException("recordingState in unknown state")
                    }
                }
            }
            isEnabled = false
        }

        captureViewBinding.stopButton.apply {
            setOnClickListener {
                isClickedStopButton = true
                startStopTimer()
                resetTimer()

                captureViewBinding.stopButton.visibility = View.INVISIBLE
                if (currentRecording == null || recordingState is VideoRecordEvent.Finalize) {
                    return@setOnClickListener
                }

                val recording = currentRecording
                if (recording != null) {
                    recording.stop()
                    currentRecording = null
                }
            }
            visibility = View.INVISIBLE
            isEnabled = false
        }

        captureLiveStatus.observe(viewLifecycleOwner) {
            captureViewBinding.captureStatus.apply { post { text = it } }
        }

        captureViewBinding.backButton.setOnClickListener {
            backToPrevious()
        }
    }

    private fun updateUI(event: VideoRecordEvent) {
        val state = if (event is VideoRecordEvent.Status) recordingState.getScopeName()
        else event.getScopeName()
        when (event) {
            is VideoRecordEvent.Status -> {}
            is VideoRecordEvent.Start -> showUI(UiState.RECORDING, event.getScopeName().value)
            is VideoRecordEvent.Finalize -> showUI(UiState.FINALIZED, event.getScopeName().value)
            is VideoRecordEvent.Pause -> {}
            is VideoRecordEvent.Resume -> {}
        }

        val stats = event.recordingStats
        val size = stats.numBytesRecorded / 1000
        val time = java.util.concurrent.TimeUnit.NANOSECONDS.toSeconds(stats.recordedDurationNanos)
        var text = "${state}: recorded ${size}KB, in ${time}second"
        if (event is VideoRecordEvent.Finalize)
            text = "${text}\nFile saved to: ${event.outputResults.outputUri}"

        captureLiveStatus.value = text
    }

    private fun enableUI(enable: Boolean) {
        arrayOf(
            captureViewBinding.cameraButton,
            captureViewBinding.captureButton,
            captureViewBinding.stopButton,
        ).forEach {
            it.isEnabled = enable
        }
        if (cameraCapabilities.size <= 1) captureViewBinding.cameraButton.isEnabled = false
    }


    private fun showUI(state: UiState, status: String = "idle") {
        captureViewBinding.let {
            when (state) {
                UiState.IDLE -> {
                    it.stopButton.visibility = View.INVISIBLE
                    it.cameraButton.visibility = View.VISIBLE
                }
                UiState.RECORDING -> {
                    it.cameraButton.visibility = View.INVISIBLE

                    it.captureButton.isEnabled = false
                    it.captureButton.visibility = View.INVISIBLE
                    it.stopButton.visibility = View.VISIBLE
                    it.stopButton.isEnabled = true
                    it.navBar.visibility = View.GONE
                    it.recordTimeView.visibility = View.VISIBLE

                }
                UiState.FINALIZED -> {
                    it.stopButton.visibility = View.INVISIBLE
                    it.captureButton.visibility = View.VISIBLE
                    it.cameraButton.visibility = View.VISIBLE

                    it.navBar.visibility = View.VISIBLE
                    it.recordTimeView.visibility = View.GONE
                }
            }
            it.captureStatus.text = status
        }
    }

    private fun resetUIandState(reason: String) {
        enableUI(true)
        showUI(UiState.IDLE, reason)

        cameraIndex = 0
        qualityIndex = DEFAULT_QUALITY_IDX
        audioEnabled = false
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PERMISSIONS_REQUIRED && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted)
                Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
        }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        captureViewBinding.labelTime.text = getTimeStringFromDouble(time)
    }

    private fun startStopTimer() {
        if (timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        requireContext().startService(serviceIntent)
        timerStarted = true
    }

    private fun stopTimer() {
        requireContext().stopService(serviceIntent)
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            captureViewBinding.labelTime.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hour, min, sec)
}