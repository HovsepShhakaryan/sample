package com.vylo.main.responsemain.createaudio.presentation.fragment

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.vylo.common.BaseFragment
import com.vylo.common.util.TimerService
import com.vylo.common.util.enums.UploadFileType
import com.vylo.graphviewlibrary.WaveSample
import com.vylo.main.R
import com.vylo.main.databinding.FragmentCreateAudioBinding
import com.vylo.main.component.sharedviewmodel.NavigationSharedViewModel
import com.vylo.main.responsemain.createaudio.presentation.common.VoiceRecorder
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.roundToInt


class CreateAudioFragment(
    private val pager: ViewPager2
) : BaseFragment<FragmentCreateAudioBinding>() {

    private val SCALE = "scale"
    private val OUTPUT_DIRECTORY = "VoiceRecorder"
    private val OUTPUT_FILENAME = "recorder.mp3"
    private val MY_PERMISSIONS_REQUEST_CODE = 0
    private lateinit var recorder: VoiceRecorder
    private lateinit var samples: List<WaveSample>
    private var scale = 8
    private var time = 0.0
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var isClickedStopButton = false
    override fun getViewBinding() = FragmentCreateAudioBinding.inflate(layoutInflater)
    private val sharedViewModel by sharedViewModel<NavigationSharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        isClickedStopButton = false
        serviceIntent = Intent(requireContext(), TimerService::class.java)
        requireContext().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
    }

    override fun onPause() {
        super.onPause()
        startStopTimer()
        resetTimer()
        viewBinder.graphView.visibility = View.INVISIBLE
        if (recorder.isRecording) {
            viewBinder.control.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_recording))
            viewBinder.graphView.stopPlotting()
            samples = recorder.stopRecording() as List<WaveSample>
            viewBinder.graphView.showFullGraph(samples)
            viewBinder.labelTime.visibility = View.GONE
        }
    }

    private fun createNavigationBar() {
        viewBinder.navBar.apply {
            viewBinder.navBar.setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            val typeface = ResourcesCompat.getFont(requireContext(), com.vylo.common.R.font.suisse_lntl_medium)
            setTitleFontFamily(typeface)
            setTitle(resources.getString(com.vylo.common.R.string.title_record_audio))
            setTitleStyle(com.vylo.common.R.style.MainText_H4)
            val buttonBack = View.OnClickListener {
                backToPrevious()
            }
            viewBinder.navBar.clickOnButtonBack(buttonBack)
        }
    }

    private fun createContentOfView(savedInstanceState: Bundle?) {
        with(viewBinder.graphView) {
            this.setGraphColor(Color.rgb(255, 255, 255))
            setCanvasColor(Color.rgb(20, 20, 20))
            setTimeColor(Color.rgb(255, 255, 255))
        }
        recorder = VoiceRecorder.getInstance()
        if (recorder.isRecording) {
            recorder.startPlotting(viewBinder.graphView)
        }
        if (savedInstanceState != null) {
            scale = savedInstanceState.getInt(SCALE)
            viewBinder.graphView.setWaveLengthPX(scale)
            if (!recorder.isRecording) {
                samples = recorder.samples as List<WaveSample>
                viewBinder.graphView.showFullGraph(samples)
            }
        }

        viewBinder.control.setOnClickListener { controlClick() }
    }

    private fun beginning(savedInstanceState: Bundle?) {
        requestPermissions()
        createNavigationBar()
        createContentOfView(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SCALE, scale)
        super.onSaveInstanceState(outState)
    }

    private fun controlClick() {
        resetTimer()
        if (recorder.isRecording) {
            isClickedStopButton = true
            viewBinder.control.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_recording))
            usePixelCopy(viewBinder.graphView.frame) { bitmap: Bitmap? ->
                saveImage(bitmap!!)
            }
            viewBinder.graphView.stopPlotting()
            samples = recorder.stopRecording() as List<WaveSample>
            viewBinder.graphView.showFullGraph(samples)
            viewBinder.labelTime.visibility = View.GONE
            viewBinder.graphView.visibility = View.INVISIBLE
            if (isClickedStopButton) {
                val uri = Uri.parse(recorder.outputFilePath)
                sharedViewModel.setUri(uri, UploadFileType.AUDIO)
            }

            Timer().schedule(1000) {
                requireActivity().runOnUiThread(Runnable {
                    pager.currentItem = 1
                })
            }
            // pager.currentItem = 1
        } else if (checkRecordPermission() && checkStoragePermission()) {
            viewBinder.graphView.visibility = View.VISIBLE
            viewBinder.labelTime.visibility = View.VISIBLE
            viewBinder.graphView.reset()
            val filepath: String = requireContext().externalCacheDir!!.absolutePath
            val file = File(filepath, OUTPUT_DIRECTORY)
            if (!file.exists()) file.mkdirs()
            recorder.outputFilePath = file.absoluteFile.toString() + "/" + OUTPUT_FILENAME
            recorder.startRecording()
            recorder.startPlotting(viewBinder.graphView)
            viewBinder.control.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_record))
        } else
        {
            requestPermissions()
        }
        startStopTimer()
    }

    private fun usePixelCopy(videoView: SurfaceView, callback: (Bitmap?) -> Unit) {
        val bitmap: Bitmap = Bitmap.createBitmap(
            videoView.width,
            videoView.height,
            Bitmap.Config.ARGB_8888
        );
        try {
            val handlerThread = HandlerThread("PixelCopier");
            handlerThread.start();
            PixelCopy.request(
                videoView, bitmap,
                { copyResult ->
                    if (copyResult == PixelCopy.SUCCESS) {
                        callback(bitmap)
                    }
                    handlerThread.quitSafely();
                },
                Handler(handlerThread.looper)
            )
        } catch (e: IllegalArgumentException) {
            callback(null)
            e.printStackTrace()
        }
    }

    private fun saveImage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().absolutePath
        val myDir = File("$root/Pictures/vylo")
        myDir.mkdirs()
        val fname = "audio_cart.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            sharedViewModel.setAudioUri(file.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_CODE
            )
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun checkRecordPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
        if (permission != PackageManager.PERMISSION_GRANTED) requestPermissions()
        else return true
        return false
    }

    private fun checkStoragePermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) requestPermissions()
        else return true
        return false
    }

    private fun resetTimer()
    {
        stopTimer()
        time = 0.0
        viewBinder.labelTime.text = getTimeStringFromDouble(time)
    }

    private fun startStopTimer()
    {
        if(timerStarted) stopTimer()
        else startTimer()
    }

    private fun startTimer()
    {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        requireContext().startService(serviceIntent)
        timerStarted = true
    }

    private fun stopTimer()
    {
        requireContext().stopService(serviceIntent)
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            viewBinder.labelTime.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double): String
    {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hour, min, sec)
}