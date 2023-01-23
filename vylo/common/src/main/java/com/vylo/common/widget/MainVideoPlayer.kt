package com.vylo.common.widget

import android.content.Context
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Bundle
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.vylo.common.R
import com.vylo.common.ext.toGone

class MainVideoPlayer(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context!!, attrs, defStyleAttr) {

    private val playerView: PlayerView
    private val volume: ImageView
    private val fullScreenEnter: ImageView
    private val fullScreenExit: ImageView

    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    private lateinit var audioManager: AudioManager

    private fun setFullScreen(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fullScreenExit.toGone()
        } else {
            fullScreenEnter.toGone()
        }
    }

    fun onFullScreenClickEnter(onClick: OnClickListener) {

        fullScreenEnter.setOnClickListener(onClick)
    }

    fun onFullScreenClickExit(onClick: OnClickListener) {
        fullScreenExit.setOnClickListener(onClick)
    }

    fun initialize(activityContext: Context) {
        val orientation = activityContext.resources.configuration.orientation
        setFullScreen(orientation)

        playerView.showController()

        initVideoPlayer(activityContext)

        volume.setOnClickListener {
            if (player?.volume ?: 0f > 0f) {
                volume.setImageResource(R.drawable.ic_volume_mute)
                player?.volume = 0f
            } else {
                volume.setImageResource(R.drawable.ic_volume)
                player?.volume = 1f
            }
        }
    }

    private fun initVideoPlayer(activityContext: Context) {
        audioManager = activityContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val trackSelector = DefaultTrackSelector(activityContext).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        player = ExoPlayer.Builder(activityContext)
            .setTrackSelector(trackSelector)
            .build()
            .also { exoPlayer ->
                val mediaItem =
                    MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()

                playerView.player = exoPlayer
            }
    }

    fun releasePlayer() {
        player?.let {
            playbackPosition = it.currentPosition
            currentWindow = it.currentMediaItemIndex
            playWhenReady = it.playWhenReady
            it.release()
        }
        player = null
    }

    fun savePlayerState(bundle: Bundle) {
        with(bundle) {
            player?.let {
                putBoolean(READY, it.playWhenReady)
                putInt(WINDOW, it.currentMediaItemIndex)
                putLong(POSITION, it.currentPosition)
            }
        }
    }

    fun restorePlayerState(bundle: Bundle) {
        with(bundle) {
            playWhenReady = getBoolean(READY)
            currentWindow = getInt(WINDOW)
            playbackPosition = getLong(POSITION)
        }
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        inflate(context, R.layout.main_video_player, this)
        playerView = findViewById(R.id.video_view)
        volume = playerView.findViewById(R.id.exo_volume)
        fullScreenEnter = playerView.findViewById(R.id.exo_fullscreen_enter)
        fullScreenExit = playerView.findViewById(R.id.exo_fullscreen_exit)
    }

    companion object {
        private const val READY = "ready"
        private const val WINDOW = "window"
        private const val POSITION = "position"
    }
}