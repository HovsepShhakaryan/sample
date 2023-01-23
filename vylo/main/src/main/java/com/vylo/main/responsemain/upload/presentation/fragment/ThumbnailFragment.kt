package com.vylo.main.responsemain.upload.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.viewpager2.widget.ViewPager2
import com.video.editor.utils.BackgroundExecutor
import com.video.editor.view.Thumb
import com.vylo.common.BaseFragment
import com.vylo.main.R
import com.vylo.main.component.sharedviewmodel.NavigationSharedViewModel
import com.vylo.main.databinding.FragmentThumbnailBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File

enum class ThumbnailType {
    BITMAP,
    GALLERY
}

class ThumbnailFragment(private val pager: ViewPager2) : BaseFragment<FragmentThumbnailBinding>() {
    override fun getViewBinding() = FragmentThumbnailBinding.inflate(layoutInflater)
    private val sharedViewModel by sharedViewModel<NavigationSharedViewModel>()
    private var videoUri: Uri? = null
    private var thumbnailUri: Uri? = null
    private var thumbnailType: ThumbnailType = ThumbnailType.BITMAP
    private var path: String? = null
    private var minRatio: Float = 1f
    private var maxRatio: Float = 1.78f
    private var videoWidth: Int = 0
    private var videoHeight: Int = 0

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                thumbnailUri = result.data!!.data

                result.data!!.data?.let { viewBinder.thumbnail.setImageURI(it) }
                thumbnailType = ThumbnailType.GALLERY
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinder = FragmentThumbnailBinding.inflate(inflater, container, false)
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun beginning() {
        sharedViewModel.responseUriThumbail.observe(viewLifecycleOwner) {
            if (it != null) {
                videoUri = it

                viewBinder.timeLineView.setVideo(videoUri!!)
                viewBinder.timeLineView.createPreview(viewBinder.timeLineView.width)
                loadFrame(0)
                val mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(context, videoUri)
                videoWidth = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()!!
                videoHeight = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()!!

                minRatio = 0.3f
                maxRatio = 3f
            }
        }

        createNavigationBar()
        setUpListeners()
        viewBinder.pickCameraroll.setOnClickListener {
            getFromCameraRoll()
        }
    }

    private fun setUpListeners() {
        viewBinder.handlerTop.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                loadFrame(seekBar?.progress ?: 0)
            }
        })
    }

    private fun createNavigationBar() {
        viewBinder.navBar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            val typeface =
                ResourcesCompat.getFont(requireContext(), com.vylo.common.R.font.suisse_lntl_medium)
            setTitleFontFamily(typeface)

            setTitle(resources.getString(R.string.label_edit_cover))
            setNextBorderButtonText(resources.getString(R.string.button_done))

            setTitleStyle(com.vylo.common.R.style.MainText_H4)
            setNextBorderButtonStyle(com.vylo.common.R.drawable.shape_transparent_rect)
            setNextBorderButtonTextStyle(com.vylo.common.R.style.MainText_H8_link)
            clickOnButtonBack { pager.currentItem = if (sharedViewModel.responseType == 2) 1 else 2 }
            clickOnButtonBorderNext {
                if (thumbnailType == ThumbnailType.BITMAP) {
                    sharedViewModel.setThumbnailBitmap(viewBinder.thumbnail.drawable.toBitmap())
                } else {
                    sharedViewModel.setThumbnailGallery(thumbnailUri)
                }
                pager.currentItem = if (sharedViewModel.responseType == 2) 1 else 2
            }
        }
    }

    private fun getFromCameraRoll() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        resultLauncher.launch(intent)
    }

    private fun loadFrame(progress: Int) {
        BackgroundExecutor.execute(object : BackgroundExecutor.Task("", 0L, "") {
            override fun execute() {
                try {
                    val mediaMetadataRetriever = MediaMetadataRetriever()
                    mediaMetadataRetriever.setDataSource(context, videoUri)
                    val videoLengthInMs = (Integer.parseInt(
                        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ) * 1000).toLong()
                    val seekDuration = (videoLengthInMs * progress) / 1000
                    val bitmap = mediaMetadataRetriever.getFrameAtTime(
                        seekDuration * 10,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                    )
                    if (bitmap != null) {
                        try {
                            (context as Activity).runOnUiThread {
                                viewBinder.thumbnail.setImageBitmap(bitmap)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    mediaMetadataRetriever.release()
                } catch (e: Throwable) {
                    Thread.getDefaultUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e)
                }
            }
        })
    }
}