package com.video.editor.view

import android.app.Activity
import android.content.Context
import android.media.MediaExtractor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.SeekBar
import com.video.editor.R
import com.video.editor.interfaces.OnCommandVideoListener
import com.video.editor.utils.BackgroundExecutor
import com.video.editor.utils.RealPathUtil
import com.video.editor.utils.VideoCommands
import kotlinx.android.synthetic.main.view_cropper.view.*
import java.io.File
import java.util.*
import kotlin.math.abs



class VideoCropper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var fileUri: Uri
    private var commandVideoListener: OnCommandVideoListener? = null
    private var path: String? = null
    private var minRatio: Float = 1f
    private var maxRatio: Float = 1.78f
    private var videoWidth: Int = 0
    private var videoHeight: Int = 0
    private var destinationPath: String
        get() {
            if (path == null) {
                val folder = Environment.getExternalStorageDirectory()
                path = folder.path + File.separator
            }
            return path ?: ""
        }
        set(finalPath) {
            path = finalPath
        }

    init {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_cropper, this, true)
        setUpListeners()
    }

    private fun setUpListeners() {
        handlerTop.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                loadFrame(seekBar?.progress ?: 0)
            }
        })
    }

    fun setVideoURI(videoURI: Uri): VideoCropper {
        fileUri = videoURI
        timeLineView.setVideo(fileUri)
        loadFrame(0)
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, fileUri)
        videoWidth = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()!!
        videoHeight = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()!!
        return this
    }

    fun save() {

    }

    fun cancel() {
        commandVideoListener?.cancelAction()
    }

    fun setOnCommandVideoListener(commandVideoListener: OnCommandVideoListener): VideoCropper {
        this.commandVideoListener = commandVideoListener
        return this
    }

    fun setDestinationPath(path: String): VideoCropper {
        destinationPath = path
        return this
    }

    fun setMinMaxRatios(minRatio: Float, maxRatio: Float): VideoCropper {
        this.minRatio = minRatio
        this.maxRatio = maxRatio
        return this
    }

    fun getThumbnailImage() {

    }

    fun setThumbnailImage(uri: Uri) {
        thumbnail.setImageURI(uri)
    }

    private fun loadFrame(progress: Int) {
        BackgroundExecutor.execute(object : BackgroundExecutor.Task("", 0L, "") {
            override fun execute() {
                try {
                    val mediaMetadataRetriever = MediaMetadataRetriever()
                    mediaMetadataRetriever.setDataSource(context, fileUri)
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
                                thumbnail.setImageBitmap(bitmap)
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