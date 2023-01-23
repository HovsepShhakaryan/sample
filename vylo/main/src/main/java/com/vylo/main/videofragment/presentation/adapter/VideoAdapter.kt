package com.vylo.main.videofragment.presentation.adapter

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.media.AudioManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.squareup.picasso.Picasso
import com.vylo.common.adapter.entity.UserInfo
import com.vylo.common.entity.VideoData
import com.vylo.common.entity.WebData
import com.vylo.common.ext.orFalse
import com.vylo.common.ext.orZero
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toVisible
import com.vylo.common.util.Converter
import com.vylo.common.util.enums.VideoType
import com.vylo.main.R
import com.vylo.main.databinding.ItemVideoHolderBinding
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.videofragment.presentation.fragment.VideoFragment


class VideoAdapter(
    private val onRespondClick: (String?, String?, String?, String?) -> Unit,
    private val onResponsesClick: (VideoData) -> Unit,
    private val onInsightfulClick: (String?, Boolean) -> Unit,
    private val onShareClick: (String?, String?) -> Unit,
    private val onKebabClick: (FeedItem, Boolean) -> Unit,
    private val onUserClick: (String?) -> Unit,
    private val navigateToWebView: (WebData) -> Unit,
    private val navigateToVideo: (VideoData) -> Unit,
    private val onFollowClick: (Long, String, Boolean) -> Unit,
    private val registerPlayer: (String, ExoPlayer) -> Unit,
    private val followingCallBack: (String) -> Unit,
    private val isMyProfile: Boolean?,
    private val myGlobalId: String?
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private val videoList = mutableListOf<VideoItem>()
    private lateinit var videoHolder: VideoViewHolder

    val listSize: Int
        get() = videoList.size

    fun getViewHolder() = videoHolder
    fun isVideoHolderInitialized() = this::videoHolder.isInitialized

    fun submitList(list: List<VideoItem>?) {
        list?.let {
            videoList.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun submitItem(item: VideoItem?) {
        item?.let {
            videoList.add(it)
            notifyDataSetChanged()
        }
    }

    fun deleteData(id: String) {
        val delItem = videoList.find { it.videoItem.globalId == id }
        delItem?.let {
            val index = videoList.indexOf(it)
            videoList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun setIsLikeStatus(id: String, isLike: Boolean) {
        if (isVideoHolderInitialized()) {
            val index = videoList.indexOfFirst { it.videoItem.globalId == id }
            videoList[index].isLike = isLike
            videoHolder.setLikeStatus(videoList[index].isLike)
        }
    }

    fun setIsFollowingStatus(globalId: String, isFollowing: Boolean) {
        if (isVideoHolderInitialized()) {
            val index = videoList.indexOfFirst { it.videoItem.publisherId == globalId }
            videoList[index].isFollowing = isFollowing
            videoHolder.setFollowingStatus(videoList[index])
        }
    }

    fun setPublisherId(globalId: String, id: Long) {
        if (isVideoHolderInitialized()) {
            val index = videoList.indexOfFirst { it.videoItem.publisherId == globalId }
            videoList[index].publisherId = id
            videoHolder.setFollowingListener(id, globalId)
        }
    }

    class VideoViewHolder(
        private val binding: ItemVideoHolderBinding,
        private val onRespondClick: (String?, String?, String?, String?) -> Unit,
        private val onResponsesClick: (VideoData) -> Unit,
        private val onInsightfulClick: (String?, Boolean) -> Unit,
        private val onShareClick: (String?, String?) -> Unit,
        private val onKebabClick: (FeedItem, Boolean) -> Unit,
        private val onUserClick: (String?) -> Unit,
        private val navigateToWebView: (WebData) -> Unit,
        private val navigateToVideo: (VideoData) -> Unit,
        private val onFollowClick: (Long, String, Boolean) -> Unit,
        private val registerPlayer: (String, ExoPlayer) -> Unit,
        private val context: Context,
        private val followingCallBack: (String) -> Unit,
        private val isMyProfile: Boolean?,
        private val myGlobalId: String?
    ) : RecyclerView.ViewHolder(binding.root), VideoFragment.CallBackEvent {

        private var player: ExoPlayer? = null
        private lateinit var audioManager: AudioManager
        private var isFullHeight: Boolean? = null

        private lateinit var volume: ImageView
        private lateinit var fullScreenEnter: ImageView
        private lateinit var fullScreenExit: ImageView
        private lateinit var seekBar: SeekBar

        fun bind(item: VideoItem, isFirst: Boolean) {

            isMyProfile?.let {
                if (!isMyProfile)
                    item.videoItem.publisherId?.let {
                        followingCallBack(it)
                    }
            }

            if (item.videoItem.contentType == "mp3") {
                binding.videoView.visibility = View.GONE
                binding.videoControllerView.visibility = View.GONE
            } else {
                binding.videoControllerViewAudio.visibility = View.GONE
            }

            initVideo(
                item.videoItem.globalId.orEmpty(),
                item.videoItem.contentUrl.orEmpty(),
                isFirst,
                item
            )

            binding.apply {
                if (item.videoItem.responseTo == null) {
                    newsTitle.text = item.videoItem.title

                    llNews.toVisible()
                    llResponse.toGone()
                } else {
                    responseView.initialize(
                        titleNews = item.videoItem.responseTo?.title.orEmpty(),
                        imageNewsCompany = item.videoItem.responseTo?.publisherImage.orEmpty(),
                        titleNewsName = item.videoItem.responseTo?.publisherName.orEmpty(),
                        imageReport = item.videoItem.responseTo?.thumbnailUrl.orEmpty(),
                        onNewsClick = {
                            if (item.videoItem.globalId?.isNotEmpty() == true) {
                                item.videoItem.responseTo.globalId?.let { id ->
                                    if (!item.videoItem.responseTo.link.isNullOrEmpty()) {
                                        navigateToWebView(
                                            WebData(
                                                id,
                                                item.videoItem.responseTo.link,
                                                item.videoItem.title,
                                                item.videoItem.externalLink,
                                                item.videoItem.categoryId,
                                                item.videoItem.categoryName
                                            )
                                        )
                                    } else if (!item.videoItem.responseTo.contentUrl.isNullOrEmpty()) {
                                        navigateToVideo(VideoData(id, videoType = VideoType.NEWS))
                                    }
                                }
                            }
                        },
                        onPublisherClick = { onUserClick(item.videoItem.responseTo.publisherId) }
                    )

                    llNews.toGone()
                    llResponse.toVisible()
                }

                UserInfo(
                    id = item.publisherId,
                    globalId = item.videoItem.publisherId,
                    reporterImage = item.videoItem.publisherImage,
                    name = item.videoItem.publisherName.orEmpty(),
                    isLabelFollowing = myGlobalId != item.videoItem.publisherId,
                    isFollower = item.isFollowing,
                    subName = item.videoItem.getUserName()
                ).apply {
                    userView.initialize(
                        info = this,
                        clickButtonFollowing = onFollowClick,
                        clickUser = { onUserClick(item.videoItem.publisherId) }
                    )
                    userView.updateNickNameStyle(ContextCompat.getColor(
                        context,
                        com.vylo.common.R.color.white_gray_ultra_light
                    ))
                }

                bottomNavBar.buttonBackVisibility(View.INVISIBLE)
                bottomNavBar.kebabVisibility(View.INVISIBLE)
                bottomNavBar.responseListVisibility(View.INVISIBLE)

                bottomNavBar.clickOnButtonRespond {
                    stopVideo()
                    onRespondClick(
                        item.videoItem.globalId,
                        item.videoItem.title,
                        item.videoItem.categoryId,
                        item.videoItem.categoryName
                    )
                }
                bottomNavBar.setButtonResponsesTitle(item.videoItem.responseCounter.orZero())
                if (item.videoItem.responseCounter.orZero() > 0) {
                    item.videoItem.globalId?.let { id ->
                        bottomNavBar.clickOnButtonResponses {
                            stopVideo()
                            onResponsesClick(VideoData(id, videoType = VideoType.RESPONSES))
                        }
                    }
                }

                like.setOnClickListener {
                    item.isLike = !item.isLike
                    onInsightfulClick(item.videoItem.globalId, item.isLike)
                }
                share.setOnClickListener {
                    onShareClick(
                        item.videoItem.globalId,
                        item.videoItem.externalLink
                    )
                }
                item.videoItem.globalId?.let { id ->
                    kebab.setOnClickListener { onKebabClick(item.videoItem, item.isLike) }
                }

                setLikeStatus(item.isLike)
            }
        }

        private fun countPercentageOfPlay(): Int {
            val total = binding.videoControllerView.player?.duration!!.toDouble()
            val current = binding.videoControllerView.player?.currentPosition!!.toDouble()
            return if (total > 0)
                ((current / total) * 100).toInt()
            else 0
        }

        private fun stopVideo() {
            binding.apply {
                videoView.player?.stop()
                videoControllerView.player?.stop()
            }
        }

        fun setLikeStatus(isLike: Boolean) {
            if (isLike) {
                binding.like.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_insightful_active
                    )
                )
            } else {
                binding.like.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_insightful_nonactive
                    )
                )
            }
        }

        fun setFollowingStatus(item: VideoItem) {
            binding.userView.updateFollowingStatus(myGlobalId != item.videoItem.publisherId, item.isFollowing)
        }

        fun setFollowingListener(id: Long, globalId: String) {
            binding.userView.updateFollowingListener(id, globalId)
        }

        private fun initVideo(id: String, contentUrl: String, isFirst: Boolean, item: VideoItem) {
            binding.apply {
                initVideoControlViewAudio(item)
                initVideoControlView()
                initVideoPlayer(id, context, contentUrl, isFirst)


                val orientation = context.resources.configuration.orientation
                setFullScreen(orientation)

                videoView.showController()

                volume.setOnClickListener {
                    if ((player?.volume ?: 0f) > 0f) {
                        volume.setImageResource(com.vylo.common.R.drawable.ic_volume_mute)
                        player?.volume = 0f
                    } else {
                        volume.setImageResource(com.vylo.common.R.drawable.ic_volume)
                        player?.volume = 1f
                    }
                }

                fullScreenEnter.setOnClickListener { fullScreenEnter() }
                fullScreenExit.setOnClickListener { fullScreenExit() }
            }
        }

        private fun fullScreenEnter() {
            setVideoScreenSize(true)
        }

        private fun fullScreenExit() {
            setVideoScreenSize(false)
        }

        private fun setVideoScreenSize(isFullScreen: Boolean) {
            binding.apply {
                if (isFullHeight == null) {
                    isFullHeight = videoView.height == videoView.videoSurfaceView?.height
                }
                if (isFullHeight.orFalse()) {
                    videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                } else {
                    videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
                }

                if (isFullScreen) {
                    fullScreenEnter.toGone()
                    fullScreenExit.toVisible()
                } else {
                    fullScreenEnter.toVisible()
                    fullScreenExit.toGone()
                }
            }
            isFullHeight = !isFullHeight.orFalse()
        }

        private fun setFullScreen(orientation: Int) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                fullScreenExit.toGone()
            } else {
                fullScreenEnter.toGone()
            }
        }

        private fun initVideoPlayer(
            id: String,
            activityContext: Context,
            url: String,
            isFirst: Boolean
        ) {
            audioManager = activityContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            val trackSelector = DefaultTrackSelector(activityContext).apply {
                setParameters(buildUponParameters().setMaxVideoSizeSd())
            }
            player = ExoPlayer.Builder(activityContext)
                .setTrackSelector(trackSelector)
                .build()
                .also { exoPlayer ->
                    val mediaItem =
                        MediaItem.fromUri(url)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.playWhenReady = true
                    exoPlayer.seekTo(0, 0)
                    exoPlayer.prepare()
                    if (!isFirst) {
                        exoPlayer.pause()
                    }

                    binding.apply {
                        videoView.player = exoPlayer
                        videoControllerView.player = exoPlayer
                        videoControllerViewAudio.player = exoPlayer
                    }

                }

            player?.let {
                registerPlayer(id, it)
            }


        }

        private fun initVideoControlView() {
            binding.videoControllerView.apply {
                volume = findViewById(R.id.exo_volume)
                fullScreenEnter = findViewById(R.id.exo_fullscreen_enter)
                fullScreenExit = findViewById(R.id.exo_fullscreen_exit)
            }
        }

        private fun initVideoControlViewAudio(item: VideoItem) {
            binding.videoControllerViewAudio.apply {
                val wave: ImageView = findViewById(R.id.wave)
                Picasso.get().load(item.videoItem.thumbnailUrl).into(wave)
                val finalTimeLabel: TextView = findViewById(R.id.exo_position_final)
                item.videoItem.contentDuration?.let {
                    finalTimeLabel.text = Converter.getDurationString(item.videoItem.contentDuration)
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onRespondClick: (String?, String?, String?, String?) -> Unit,
                onResponsesClick: (VideoData) -> Unit,
                onInsightfulClick: (String?, Boolean) -> Unit,
                onShareClick: (String?, String?) -> Unit,
                onKebabClick: (FeedItem, Boolean) -> Unit,
                onUserClick: (String?) -> Unit,
                navigateToWebView: (WebData) -> Unit,
                navigateToVideo: (VideoData) -> Unit,
                onFollowClick: (Long, String, Boolean) -> Unit,
                registerPlayer: (String, ExoPlayer) -> Unit,
                followingCallBack: (String) -> Unit,
                isMyProfile: Boolean?,
                myGlobalId: String?
            ): VideoViewHolder {
                return VideoViewHolder(
                    ItemVideoHolderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onRespondClick,
                    onResponsesClick,
                    onInsightfulClick,
                    onShareClick,
                    onKebabClick,
                    onUserClick,
                    navigateToWebView,
                    navigateToVideo,
                    onFollowClick,
                    registerPlayer,
                    parent.context,
                    followingCallBack,
                    isMyProfile,
                    myGlobalId
                )
            }
        }

        override fun callBackEvent() = countPercentageOfPlay()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VideoViewHolder.create(
            parent,
            onRespondClick,
            onResponsesClick,
            onInsightfulClick,
            onShareClick,
            onKebabClick,
            onUserClick,
            navigateToWebView,
            navigateToVideo,
            onFollowClick,
            registerPlayer,
            followingCallBack,
            isMyProfile,
            myGlobalId
        )

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        videoHolder = holder
        holder.bind(videoList[position], position == 0)
    }

    override fun getItemCount() = videoList.size
}