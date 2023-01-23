package com.vylo.main.videofragment.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.size
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.ExoPlayer
import com.vylo.common.BaseFragment
import com.vylo.common.entity.*
import com.vylo.common.ext.orFalse
import com.vylo.common.ext.shareLink
import com.vylo.common.ext.toClipboard
import com.vylo.common.util.*
import com.vylo.common.util.enums.ResponseType
import com.vylo.common.util.enums.VideoType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.component.events.domain.entity.request.VyloView
import com.vylo.main.component.events.presentation.EventsViewModel
import com.vylo.main.component.kebab.domain.entity.response.EventCounterData
import com.vylo.main.databinding.FragmentVideoBinding
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.homefragment.domain.mappers.toVideoItem
import com.vylo.main.homefragment.domain.mappers.toVideoItemList
import com.vylo.main.profilefragment.common.ProfileData
import com.vylo.main.profilefragment.domain.entity.response.toNewsItem
import com.vylo.main.profilefragment.presentation.fragment.DeleteDialogFragment
import com.vylo.main.videofragment.presentation.adapter.ExoManager
import com.vylo.main.videofragment.presentation.adapter.VideoAdapter
import com.vylo.main.videofragment.presentation.viewmodel.VideoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoFragment : BaseFragment<FragmentVideoBinding>(), LifecycleEventObserver {

    interface CallBackEvent {
        fun callBackEvent(): Int
    }

    interface CallBackEventMain {
        fun callBackEventMain(vyloView: VyloView)
    }

    private lateinit var callBackEvent: CallBackEvent
    private val viewModel by viewModel<VideoViewModel>()
    private val eventViewModel by viewModel<EventsViewModel>()
    private lateinit var activity: MainFlawActivity
    override fun getViewBinding() = FragmentVideoBinding.inflate(layoutInflater)
    private val playerList = mutableListOf<ExoManager>()
    private lateinit var onPageChangeCallback: ViewPager2.OnPageChangeCallback

    private var isAvailableFeed = true
    private var isFirstTimeCall = true
    private var videoData: VideoData? = null
    private var profileType: Boolean? = null

    private var previousPosition = 0

    private lateinit var events: EventCounterData

    companion object {
        private lateinit var callBackEventMain: CallBackEventMain

        fun setCallBackEventMain(callBackEventMain: CallBackEventMain) {
            this.callBackEventMain = callBackEventMain
        }
    }

    private val videoAdapter: VideoAdapter by lazy {
        VideoAdapter(
            onRespondClick = ::onRespondClick,
            onResponsesClick = ::onResponsesClick,
            onInsightfulClick = ::onInsightfulClick,
            onShareClick = ::onShareClick,
            onKebabClick = ::onKebabClick,
            onUserClick = ::onUserClick,
            navigateToWebView = ::navigateToWebView,
            navigateToVideo = ::navigateToVideo,
            onFollowClick = ::onFollowingClick,
            registerPlayer = ::registerPlayer,
            followingCallBack = :: followingCallBack,
            profileType,
            viewModel.myGlobalId
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainFlawActivity
        activity.lifecycle.addObserver(this)
        activity.showProgress()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoData = arguments?.getParcelable(VIDEO_DATA)
        profileType = arguments?.getBoolean(PROFILE_TYPE)
        videoData?.globalId?.let { id ->
            if (isFirstTimeCall) {
                when (videoData?.videoType ?: VideoType.NEWS) {
                    VideoType.NEWS,
                    VideoType.RESPONSE -> {
                        when (profileType) {
                            true -> viewModel.getMyFeedById(id)
                            false -> viewModel.getFeedById(id)
                            else -> {}
                        }
                    }
                    VideoType.RESPONSES -> {
                        viewModel.getResponses(id)
                    }
                }
                isFirstTimeCall = false
            }
        }
        beginning()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            activity.hideNavBar()
            activity.showProgress()
        }
    }

    override fun onStart() {
        super.onStart()
        activity.hideProgress()
    }

    override fun onStop() {
        super.onStop()
        playerList.forEach {
            it.exoPlayer.stop()
        }
    }

    override fun onPause() {
        super.onPause()
        activity.hideProgress()
        if (videoAdapter.isVideoHolderInitialized()) {
            callBackEvent = videoAdapter.getViewHolder()
            val percent = callBackEvent.callBackEvent()
            callBackEventMain.callBackEventMain(
                VyloView(
                    percent.toString(),
                    videoData!!.globalId
                )
            )
        }
    }

    private fun beginning() {
        setViewSettings()
        createToolbar()

        viewModel.videoError.observe(viewLifecycleOwner) {
            hideProgress()
            errorResult(it)
            if (it == "Not found.") {
                setFragmentResult(
                    VIDEO_NOT_ALIVE,
                    bundleOf(VIDEO_NOT_ALIVE to videoData?.globalId.orEmpty())
                )
                backToPrevious()
            }
        }

        viewModel.eventError.observe(viewLifecycleOwner) {
            hideProgress()
            errorResult(it)
        }

        viewModel.eventSuccess.observe(viewLifecycleOwner) {
            events = it
            videoAdapter.setIsLikeStatus(it.globalId, it.eventCounterItem.iLikeIt.orFalse())
        }
        viewModel.videoListSuccess.observe(viewLifecycleOwner) {
            activity.hideProgress()
            videoAdapter.submitList(it.toVideoItemList())
            it.forEach { item ->
                item.globalId?.let { id ->
                    viewModel.getEventCounter(id)
                }
                item.publisherId?.let { id ->
//                    viewModel.isFollowing(id)
                    viewModel.getPublisherId(id)
                }
            }
        }

        viewModel.videoSuccess.observe(viewLifecycleOwner) {
            activity.hideProgress()
            videoAdapter.submitItem(it.toVideoItem())
            it.publisherId?.let { id ->
//                viewModel.isFollowing(id)
                viewModel.getPublisherId(id)
            }
        }

        viewModel.deleteResponseSuccess.observe(viewLifecycleOwner) {
            videoAdapter.deleteData(it)
            playerList[previousPosition].exoPlayer.stop()
            playerList.removeAt(previousPosition)
            showMessage(resources.getString(R.string.deleted_news))
            if (videoAdapter.listSize == 0) {
                backToPrevious()
            }
        }

        viewModel.isFollowing.observe(viewLifecycleOwner) {
            videoAdapter.setIsFollowingStatus(it.keys.first(), it.values.first())
        }

        viewModel.publisherId.observe(viewLifecycleOwner) {
            videoAdapter.setPublisherId(it.keys.first(), it.values.first())
        }

        viewBinder.apply {
            pager.adapter = videoAdapter
        }

    }

    private fun followingCallBack(id: String) {
        viewModel.isFollowing(id)
    }

    private fun createToolbar() {
        viewBinder.toolbar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack { backToPrevious() }
        }
    }

    private fun registerPlayer(id: String, player: ExoPlayer) {
        val index = playerList.indexOfFirst { it.globalId == id }
        if (index == -1) {
            playerList.add(ExoManager(globalId = id, exoPlayer = player))
        } else {
            playerList[index].exoPlayer.stop()
            playerList.removeAt(index)
            playerList.add(index, ExoManager(globalId = id, exoPlayer = player))
        }
    }

    private fun setViewSettings() {
        viewBinder.apply {
            onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (previousPosition != position) {
                        playerList[position].exoPlayer.seekTo(0, 0)
                        playerList[position].exoPlayer.play()

                        playerList[previousPosition].exoPlayer.seekTo(0, 0)
                        playerList[previousPosition].exoPlayer.pause()

                        previousPosition = position
                    }
                    if (position == pager.size) {
                        videoData?.globalId?.let {
                            if (isAvailableFeed) {
                                viewModel.getResponses(it)
                            }
                        }
                    }
                }
            }

            pager.registerOnPageChangeCallback(onPageChangeCallback)
        }
    }

    private fun errorResult(errorMsg: String) {
        isAvailableFeed = false
        activity.hideProgress()
        showMessage(errorMsg)
    }

    private fun onKebabClick(feedItem: FeedItem, isLike: Boolean) {
        if (viewModel.myGlobalId == feedItem.publisherId) {
            KebabManager.createMyNewsKebab(
                activity = requireActivity(),
                item = feedItem.toNewsItem(),
                isActive = isLike,
                onInsightfulClick = ::onInsightfulClick,
                onEditClick = ::onEditClick,
                onShareClick = ::onShareClick,
                onCopyLinkClick = ::onCopyLinkClick,
                onDeleteClick = ::onDeleteClick
            )
        } else {
            KebabManager.createCommonKebab(
                activity = requireActivity(),
                globalId = feedItem.globalId.orEmpty(),
                externalLink = feedItem.externalLink,
                isUserNews = feedItem.isUserNews,
                isActive = isLike,
                onInsightfulClick = ::onInsightfulClick,
                onShareClick = ::onShareClick,
                onCopyLinkClick = ::onCopyLinkClick,
                onReportClick = ::onReportClick,
            )
        }

        viewModel.eventError.observe(viewLifecycleOwner) {
            hideProgress()
            errorResult(it)
        }
    }

    private fun onInsightfulClick(id: String?, isActive: Boolean) {
        id?.let {
            videoAdapter.setIsLikeStatus(it, isActive)
            if (isActive) {
                sendAnalyticEvent(GoogleAnalytics.INSIGHTFUL, GoogleAnalytics.LIKE_KEBAB)
                viewModel.addLike(it)
            } else {
                viewModel.deleteLike(it)
            }
        }
    }

    private fun onEditClick(item: NewsItem) {
        UploadData(
            id = item.globalId.orEmpty(),
            url = item.contentUrl.orEmpty(),
            title = item.title.orEmpty(),
            categoryId = item.categoryId.orEmpty(),
            categoryName = item.categoryName.orEmpty(),
            responseToGlobalId = item.responseToGlobalId.orEmpty(),
            responseToTitle = item.responseToTitle.orEmpty(),
            responseToCategoryId = item.responseToCategoryId.orEmpty(),
            responseToCategoryName = item.responseToCategoryName.orEmpty()
        ).apply {
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    UPLOAD_INFO to this,
                    RESPONSE_TYPE to ResponseType.UPLOAD.type,
                    DESTINATION_OF_NAVIGATION_FRAGMENT to true
                )
            )
        }
    }

    private fun onDeleteClick(id: String) {
        DeleteDialogFragment {
            viewModel.deleteNews(id)
        }.show(requireActivity().supportFragmentManager, null)
    }

    private fun onShareClick(id: String?, link: String?) {
        if (!id.isNullOrEmpty() && !link.isNullOrEmpty()) {
            sendAnalyticEvent(GoogleAnalytics.SHARE, GoogleAnalytics.LIKE_NEWS)
            viewModel.shareReport(id)
            context?.shareLink(link)
        }
    }

    private fun onCopyLinkClick(id: String, link: String?) {
        if (id.isNotEmpty() && !link.isNullOrEmpty()) {
            viewModel.shareReport(id)
            context?.toClipboard(link)
            showMessage(resources.getString(R.string.label_clipboard))
        }
    }

    private fun onReportClick(id: String) {
        KebabManager.createReportKebab(
            activity = requireActivity(),
            id = id,
            sendReport = { status, globalId ->
                viewModel.sendReport(status, globalId)
            }
        )
    }

    private fun navigateToWebView(data: WebData) {
        data.globalId?.let {
            viewModel.viewReport(it)
        }
        navigateTo(R.id.webFragment, bundleOf(WEB_DATA to data))
    }

    private fun navigateToVideo(data: VideoData) {
        viewModel.viewReport(data.globalId)
        navigateTo(
            R.id.videoFragment, bundleOf(
                VIDEO_DATA to data
            )
        )
    }

    private fun onUserClick(id: String?) {
        id?.let {
            navigateTo(
                when (getMainGraph()) {
                    false -> R.id.profileFragment
                    true -> R.id.mainProfileFragment
                },
                bundleOf(USER_PROFILE to ProfileData(it))
            )
        }
    }

    private fun onRespondClick(
        id: String?,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
        val titleSheet = resources.getString(R.string.label_response)
        activity.openBottomSheetDialog(id, title, categoryId, categoryName, titleSheet)
    }

    private fun onResponsesClick(data: VideoData) {
        navigateTo(
            R.id.videoFragment, bundleOf(
                VIDEO_DATA to data
            )
        )
    }

    private fun onFollowingClick(id: Long, globalId: String, isFollowing: Boolean) {
        videoAdapter.setIsFollowingStatus(globalId, isFollowing)
        if (isFollowing) {
            viewModel.subscribePublisher(id)
        } else {
            viewModel.unsubscribePublisher(id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setVideoBrakeCall(false)
        viewBinder.pager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    override fun openCreateResponseScreen(
        id: String?,
        responseType: Int,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
        super.openCreateResponseScreen(id, responseType, title, categoryId, categoryName)
        if (!id.isNullOrEmpty())
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPOND_INFO to RespondData(
                        responseToGlobalId = id,
                        title!!,
                        categoryId!!,
                        categoryName!!
                    ),
                    RESPONSE_TYPE to responseType
                )
            )
        else
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPONSE_TYPE to responseType
                )
            )
    }

}