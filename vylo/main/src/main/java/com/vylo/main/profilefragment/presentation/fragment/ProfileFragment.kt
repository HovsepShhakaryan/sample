package com.vylo.main.profilefragment.presentation.fragment

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.vylo.common.BaseFragment
import com.vylo.common.dialog.SimpleDialogFragment
import com.vylo.common.entity.*
import com.vylo.common.ext.*
import com.vylo.common.util.*
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ResponseType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.component.entity.ProfileInfo
import com.vylo.main.component.entity.ProfileItem
import com.vylo.main.component.entity.mappers.toProfileInfo
import com.vylo.main.component.events.domain.entity.request.VyloView
import com.vylo.main.component.events.presentation.EventsViewModel
import com.vylo.main.component.kebab.domain.entity.response.EventCounterData
import com.vylo.main.component.sharedviewmodel.ActivityFragmentSharedViewModel
import com.vylo.main.databinding.FragmentProfileBinding
import com.vylo.main.followmain.followfragment.common.FollowType
import com.vylo.main.followmain.followfragment.common.FollowUserData
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.profilefragment.common.ProfileData
import com.vylo.main.profilefragment.domain.entity.request.NewsUpdateRequest
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsItem
import com.vylo.main.profilefragment.domain.entity.response.toNewsItem
import com.vylo.main.profilefragment.presentation.adapter.ProfileNewsAdapter
import com.vylo.main.profilefragment.presentation.viewmodel.ProfileViewModel
import com.vylo.main.settingsprivacy.presentation.viewmodel.SettingsSharedViewModel
import com.vylo.main.videofragment.presentation.fragment.VideoFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment<FragmentProfileBinding>(),
    SwipeRefreshLayout.OnRefreshListener, VideoFragment.CallBackEventMain {

    private val sharedViewModel: ActivityFragmentSharedViewModel by activityViewModels()

    private val viewModel by viewModel<ProfileViewModel>()
    private val settingsViewModel by sharedViewModel<SettingsSharedViewModel>()
    private val viewModelEvents by viewModel<EventsViewModel>()
    private lateinit var scrollBottomListener: ScrollBottomListener

    private lateinit var activity: MainFlawActivity
    private var profileInfo: ProfileInfo? = null
    private var userProfile: ProfileData? = null
    private var isFollow: Boolean = false
    private var isEnableCallForData = false
    private var isAvailableFeed = true
    private var categoryName: String = ""
    private var isFromProfile = true

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ProfileNewsAdapter(
            requireContext(),
            onVideoClick = ::onVideoClick,
            onNewsClick = ::onWebClick,
            onKebabClick = ::showKebab,
            onRespondClick = ::onRespondClick,
            onResponsesClick = ::onResponsesClick
        )
    }

    private val events = mutableListOf<EventCounterData>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainFlawActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onStart() {
        super.onStart()
        userProfile?.let {
//            activity.hideNavBar()
        } ?: run {
            activity.showNavBar()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.setBrakeCall(false)
    }

    override fun onResume() {
        super.onResume()
        activity.smoothContentToTop {
            viewBinder.listFeed.smoothScrollToPosition(0)
            viewBinder.nestedScrolling.fullScroll(ScrollView.FOCUS_UP)
        }

        setFragmentResultListener(UPLOAD_INFO) { key, bundle ->
            bundle.getParcelable<UploadData>(key)?.let {
                NewsUpdateRequest(
                    title = it.title,
                    categoryGlobalId = it.categoryId
                ).apply {
                    categoryName = it.categoryName
                    viewModel.updateNews(id = it.id, data = this)
                }
            }
        }
        activity.getData()?.takeUnless { it.getString(CATEGORY_NAME).isNullOrEmpty() }?.let {
            lifecycleScope.launch {
                delay(1)
                navigateTo(R.id.uploadSuccessFragment, it)
                activity.setData(null)
            }
        }
    }

    override fun onPause() {
        super.onPause()
//        activity.hideNavBar()
        if (adapter.getTimingList().isNotEmpty()) {
            val globalIdList = mutableListOf<String>()
            for (item in adapter.getTimingList()) {
                if (item.destroyedTime != null) {
                    val seconds = item.destroyedTime - item.addedTime!!
                    if (seconds in 3..9) {
                        item.item?.let {
                            globalIdList.add(it.globalId!!)
                        }
                    }
                }
            }
            if (globalIdList.size > 0)
                viewModelEvents.eventView(globalIdList)
        }

        viewBinder.userBio.collapse()
    }

    private fun beginning() {

        getDeepLinkData()

        arguments?.getParcelable<ProfileData>(USER_PROFILE)?.let {
            userProfile = it
            arguments?.remove(USER_PROFILE)
        }
        setViewSettings()
        createContentOfView()
        arguments?.getBoolean(FOLLOWING_BACK_TYPE)?.let {
            isFromProfile = it
            arguments?.remove(FOLLOWING_BACK_TYPE)
        }
    }

    private fun getDeepLinkData() {
        if (sharedViewModel.getDeepLinkId() != null) {
            userProfile = ProfileData(
                id = sharedViewModel.getDeepLinkId()!!
            )
            sharedViewModel.setDeepLinkId(null)
        }
    }

    private fun setViewSettings() {
        viewBinder.apply {
            swipeToRefresh.setOnRefreshListener(this@ProfileFragment)
        }
    }

    private fun refreshData() {
        scrollBottomListener.isSilent(false)
        isAvailableFeed = true
        viewBinder.swipeToRefresh.isRefreshing = true
        viewModel.setBrakeCall(false)
        adapter.clearData()
    }

    override fun onRefresh() {
        refreshData()
        createContentOfView()
    }

    private fun getProfileData(isShowProgressBar: Boolean) {
        scrollBottomListener.isSilent(true)
        if (isShowProgressBar) activity.showProgress()

        userProfile?.let {
            lifecycleScope.launch {
                viewModel.getUserProfile(it.id)
                viewModel.getMyProfile()
                viewModel.getUserProfileNews(it.id)
            }
        } ?: run {
            lifecycleScope.launch {
                viewModel.getProfile()
                viewModel.getProfileNews()
            }
        }
    }

    private fun createToolbar(login: String) {
        viewBinder.toolbar.apply {
            if (userProfile != null) {
                setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
                clickOnButtonBack {
                    if (userProfile != null) {
                        activity.showNavBar()
                    }
                    backToPrevious()
                }
            }
//            val typeface =
//                ResourcesCompat.getFont(requireContext(), com.vylo.common.R.font.suisse_lntl_semi_bold)
//            setTitleFontFamily(typeface)
            setTitle(login)
            setTitleStyle(com.vylo.common.R.style.Profile_user_handle)
        }
    }

    private fun createContentOfView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        scrollBottomListener = object : ScrollBottomListener(linearLayoutManager) {
            override fun onScrolledToBottom(): Boolean {
                if (isEnableCallForData && isAvailableFeed) {
                    getProfileData(true)
                    scrollBottomListener.isSilent(true)
                } else isEnableCallForData = true
                return false
            }
        }
        viewBinder.listFeed.addOnScrollListener(scrollBottomListener)
        viewBinder.listFeed.layoutManager = linearLayoutManager

        viewBinder.nestedScrolling.viewTreeObserver
            .addOnScrollChangedListener {
                viewBinder.listFeed.isNestedScrollingEnabled = (viewBinder.nestedScrolling.getChildAt(0).bottom
                        <= viewBinder.nestedScrolling.height + viewBinder.nestedScrolling.scrollY)
            }

        getProfileData(true)

        ContextCompat.getDrawable(requireContext(), R.drawable.shape_feed_divider)
            ?.let {
                val itemDecor =
                    DividerItemDecoration(context, ClipDrawable.HORIZONTAL)
                itemDecor.setDrawable(it)
                viewBinder.listFeed.addItemDecoration(itemDecor)
            }
        viewBinder.listFeed.adapter = adapter
        viewModel.responseProfileError.observe(viewLifecycleOwner) {
            errorResult(it)
        }

        viewModel.responseMyProfileError.observe(viewLifecycleOwner) {
            errorResult(it)
        }

        viewModel.responseNewsError.observe(viewLifecycleOwner) {
            errorResult(it)
        }

        viewModel.updateResponseSuccess.observe(viewLifecycleOwner) {
            adapter.updateData(
                UploadData(
                    id = it.globalId.orEmpty(),
                    url = "",
                    title = it.title.orEmpty(),
                    categoryId = it.categoryGlobalId.orEmpty(),
                    categoryName = categoryName,
                    status = it.status
                )
            )
        }

        viewModel.deleteResponseSuccess.observe(viewLifecycleOwner) {
            adapter.deleteData(it)
            showMessage(resources.getString(R.string.deleted_news))
        }

        viewModel.responseNewsSuccess.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                viewBinder.apply {
                    listFeed.toGone()
                    emptyState.toVisible()
                    emptyState.createProfileEmptyStateNoButton()
                }
            } else {
                viewBinder.apply {
                    listFeed.toVisible()
                    emptyState.toGone()
                }
                it.forEach {
                    it.globalId?.let { id ->
                        viewModel.getEventCounter(id)
                    }
                }
                successResult(false)
                adapter.submitData(it)
            }
        }

        viewModel.eventSuccess.observe(viewLifecycleOwner) {
            events.removeIf { event ->
                event.globalId == it.globalId
            }
            events.add(it)
        }

        viewModel.responseProfileSuccess.observe(viewLifecycleOwner) { profile ->
            profileInfo = profile.toProfileInfo()
            settingsViewModel.setProfileInfo(profile.toProfileInfo())
            successResult(true)
            createToolbar(profile.getUserName())

            viewBinder.apply {
                cardImg.toVisible()

                userProfile?.let {
                    viewBinder.buttonFollowing.setOnClickListener {
                        isFollow = !isFollow
                        setFollowButtonText(isFollow)
                        if (isFollow) {
                            viewModel.follow(profile.id.orZero())
                        } else {
                            viewModel.unfollow(profile.id.orZero())
                        }
                    }
                    viewModel.responseMyProfileSuccess.observe(viewLifecycleOwner) { myProfile ->
                        isFollow =
                            myProfile.publishersSubscription?.any { it.globalId == userProfile?.id }
                                .orFalse()

                        setFollowButtonText(isFollow)
                        buttonFollowing.toVisible()
                    }
                } ?: run {
                    activity.toVisible()
                    editProfile.toVisible()
                }
                hamburger.toVisible()

                Picasso.get().load(profile.profilePhoto)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.portrait_placeholder)
                    .into(imgUser)

                editProfile.apply {
                    roundedGrayWhiteTextButtonStyle(
                        requireContext(),
                        resources.getString(R.string.button_edit_profile),
                        ButtonStyle.ROUNDED_SMALL
                    )
                    setOnClickListener {
                        Bundle().apply {
                            putParcelable(
                                PROFILE_INFO, profile.toProfileInfo()
                            )
                        }.let {
                            navigateTo(R.id.toEditProfileFragment, it)
                        }
                    }
                    val verticalPaddingId = com.vylo.common.R.dimen.margin_padding_size_medium_mid
                    val horizontalPaddingId = com.vylo.common.R.dimen.margin_padding_size_small
                    setTitleMargin(
                        verticalPaddingId,
                        horizontalPaddingId,
                        verticalPaddingId,
                        horizontalPaddingId
                    )
                }
                activity.setOnClickListener {
                    navigateTo(R.id.action_mainProfileFragment_to_activityFragment2)
                }
                hamburger.setOnClickListener {
                    userProfile?.let {
                        showOtherUserProfileHamburger(profile.email.orEmpty())
                    } ?: run {
                        showHamburger()
                    }
                }

                userName.text = profile.name
                userBio.setTextOrGone(profile.bio)
                userLink.setTextOrGone(profile.website)
                profile.website?.let { link ->
                    userLink.setOnClickListener {
                        onWebSiteClick(link)
                    }
                }

                userPost.initialize(
                    resources.getString(R.string.label_profile_posts),
                    (profile.postCounter ?: 0)
                )
                userFollowing.initialize(
                    resources.getString(R.string.label_profile_following),
                    profile.followingCounter ?: 0
                )
                userFollowers.initialize(
                    resources.getString(R.string.label_profile_followers),
                    profile.followersCounter ?: 0
                )

                userFollowing.setOnClickListener {
                    navigateTo(
                        R.id.toFollowFragment,
                        getFollowBundleData(profile, FollowType.FOLLOWING, userProfile, isFromProfile)
                    )
                }
                userFollowers.setOnClickListener {
                    navigateTo(
                        R.id.toFollowFragment,
                        getFollowBundleData(profile, FollowType.FOLLOWERS, userProfile, isFromProfile)
                    )
                }
            }
        }
    }

    private fun showOtherUserProfileHamburger(link: String) {
        KebabManager.createUserHamburger(
            activity = requireActivity(),
            onCopyUrlClick = {
                context?.toClipboard(link)
                showMessage(resources.getString(R.string.label_clipboard))
            },
            onShareClick = {
                context?.shareLink(link)
            },
            onBlockUserClick = {
                SimpleDialogFragment(
                    title = resources.getString(
                        R.string.title_block_user,
                        profileInfo?.getUserName()
                    ),
                    message = resources.getString(
                        R.string.message_block_user,
                        profileInfo?.getUserName()
                    ),
                    okButtonTitle = resources.getString(R.string.label_block),
                    cancelButtonTitle = resources.getString(R.string.dialog_cancel),
                    onOkClick = {
                        sendAnalyticEvent(GoogleAnalytics.BLOCK_USER, GoogleAnalytics.BLOCK_USER_VALUE)
                        viewModel.follow(profileInfo?.id.orZero())
                        viewModel.blockUser(userProfile!!.id)
                    }
                )
                    .show(requireActivity().supportFragmentManager, null)
            },
            onReportUserClick = {
                onReportClick(profileInfo?.id.orZero())
            }
        )
    }

    private fun showHamburger() {
        KebabManager.createMyHamburger(
            activity = requireActivity(),
            onInsightfulClick = { navigateTo(R.id.action_mainProfileFragment_to_insightfulFragment2) },
            onMyContactClick = { showMessage("Contacts") },
            onSettingsPrivacyClick = {
                activity.setData(null)
                profileInfo?.let {
                    settingsViewModel.setProfileInfo(it)
                }
                navigateTo(
                    R.id.toSettingsPrivacyFragment,
                    bundleOf(PROFILE_INFO to profileInfo)
                )
            }
        )
    }

    private fun onReportClick(id: Long) {
        KebabManager.createProfileReport(
            activity = requireActivity(),
            sendReport = { status, globalId ->
                sendAnalyticEvent(GoogleAnalytics.REPORT, status.toString())
//                viewModel.sendReport(index + 1, id)
//                showMessage(globalId)
            }
        )
    }

    private fun onWebClick(data: WebData, feedItem: ProfileNewsItem?) {
        feedItem?.let {
            sendEvent(feedItem)
        }
        data.globalId?.let {
            viewModel.viewReport(it)
        }
        navigateTo(R.id.webFragment, bundleOf(WEB_DATA to data))
    }

    private fun onWebSiteClick(url: String) {
        navigateTo(R.id.webFragment, bundleOf(WEB_DATA to WebData(url = url)))
    }

    private fun onVideoClick(data: VideoData, feedItem: ProfileNewsItem?) {
        feedItem?.let {
            sendEvent(feedItem)
        }
        VideoFragment.setCallBackEventMain(this)
        var isMyProfile = true
        userProfile?.let {
            isMyProfile = false
        } ?: run {
            isMyProfile = true
        }
        viewModel.viewReport(data.globalId)
        navigateTo(
            R.id.videoFragment, bundleOf(
                VIDEO_DATA to data,
                PROFILE_TYPE to isMyProfile
            )
        )
    }

    private fun showKebab(item: ProfileNewsItem) {
        val isActive =
            events.find { it.globalId == item.globalId }?.eventCounterItem?.iLikeIt.orFalse()
        if (viewModel.myGlobalId == item.publisherId) {
            when (item.status) {
                2,4 -> KebabManager.createMyNewsKebabPrivate(
                    activity = requireActivity(),
                    item = item.toNewsItem(),
                    isActive = isActive,
                    onInsightfulClick = ::onInsightfulClick,
                    onEditClick = ::onEditClick,
                    onShareClick = ::onShareClick,
                    onCopyLinkClick = ::onCopyLinkClick,
                    onDeleteClick = ::onDeleteClick
                )
                3 -> KebabManager.createMyNewsKebab(
                    activity = requireActivity(),
                    item = item.toNewsItem(),
                    isActive = isActive,
                    onInsightfulClick = ::onInsightfulClick,
                    onEditClick = ::onEditClick,
                    onShareClick = ::onShareClick,
                    onCopyLinkClick = ::onCopyLinkClick,
                    onDeleteClick = ::onDeleteClick
                )
            }

        } else {
            when (item.status) {
                2,4 -> KebabManager.createMyNewsKebabPrivate(
                    activity = requireActivity(),
                    item = item.toNewsItem(),
                    isActive = isActive,
                    onInsightfulClick = ::onInsightfulClick,
                    onEditClick = ::onEditClick,
                    onShareClick = ::onShareClick,
                    onCopyLinkClick = ::onCopyLinkClick,
                    onDeleteClick = ::onDeleteClick
                )
                else -> KebabManager.createCommonKebab(
                    activity = requireActivity(),
                    globalId = item.globalId.orEmpty(),
                    externalLink = item.externalLink,
                    isUserNews = item.isUserNews,
                    isActive = isActive,
                    onInsightfulClick = ::onInsightfulClick,
                    onShareClick = ::onShareClick,
                    onCopyLinkClick = ::onCopyLinkClick,
                    onReportClick = ::onReportClick,
                )
            }
        }
    }

    private fun onInsightfulClick(id: String, isActive: Boolean) {
        events.find { it.globalId == id }?.let {
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

    private fun onShareClick(id: String?, data: Any?) {
        data?.let {
            id?.let { globalId ->
                sendAnalyticEvent(GoogleAnalytics.SHARE, GoogleAnalytics.LIKE_NEWS)
                viewModel.shareReport(globalId)
                context?.shareLink(it as String)
            }
        }
    }

    private fun onCopyLinkClick(id: String?, data: Any?) {
        data?.let {
            id?.let { globalId ->
                viewModel.shareReport(globalId)
                context?.toClipboard(it as String)
                showMessage(resources.getString(R.string.label_clipboard))
            }
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

    private fun successResult(isSilent: Boolean) {
        scrollBottomListener.isSilent(isSilent)
        viewBinder.swipeToRefresh.isRefreshing = false
        activity.hideProgress()
    }

    private fun errorResult(message: String) {
        isAvailableFeed = false
        isEnableCallForData = false
        scrollBottomListener.isSilent(true)
        viewBinder.swipeToRefresh.isRefreshing = false
        activity.hideProgress()
        showMessage(message)
    }

    private fun onDeleteClick(id: String) {
        DeleteDialogFragment {
            viewModel.deleteNews(id)
        }.show(requireActivity().supportFragmentManager, null)
    }

    private fun getFollowBundleData(
        profile: ProfileItem,
        type: FollowType,
        profileData: ProfileData? = null,
        isFromProfile: Boolean
    ): Bundle {
        val user = FollowUserData(
            id = profile.id ?: 0,
            userName = profile.username.orEmpty(),
            isApproved = false,
            followersCounter = profile.followersCounter ?: 0,
            followingCounter = profile.followingCounter ?: 0,
            isMyProfile = viewModel.myGlobalId == profile.globalId
        )
        return bundleOf(
            FOLLOW_TYPE to type.ordinal,
            FOLLOW_USER_DATA to user,
            USER_PROFILE to profileData,
            FOLLOWING_BACK_TYPE to isFromProfile
        )
    }

    private fun setFollowButtonText(isFollower: Boolean) {
        if (isFollower)
            viewBinder.buttonFollowing.roundedGrayWhiteTextButtonStyle(
                requireContext(),
                resources.getString(com.vylo.common.R.string.button_following),
                ButtonStyle.ROUNDED_SMALL
            )
        else
            viewBinder.buttonFollowing.roundedWhiteButtonStyle(
                requireContext(),
                resources.getString(com.vylo.common.R.string.button_follow),
                ButtonStyle.ROUNDED_SMALL
            )
    }

    private fun onRespondClick(
        id: String?,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
        sharedViewModel.setRespondType(GoogleAnalytics.CREATE_RESPONSES)
        val titleSheet = resources.getString(R.string.label_response)
        activity.openBottomSheetDialog(id, title, categoryId, categoryName, titleSheet)
    }

    private fun onResponsesClick(data: VideoData) {
        VideoFragment.setCallBackEventMain(this)
        navigateTo(
            R.id.videoFragment, bundleOf(
                VIDEO_DATA to data
            )
        )
    }

    private fun navigateToNewsstand() {
        activity.navigateTo(R.id.news_stand_main_graph)
    }

    override fun onDestroy() {
        super.onDestroy()
//        viewModel.setBrakeCall(false)
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
                    RESPONSE_TYPE to responseType,
                    DESTINATION_OF_NAVIGATION_FRAGMENT to true
                )
            )
        else
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPONSE_TYPE to responseType,
                    DESTINATION_OF_NAVIGATION_FRAGMENT to true
                )
            )
    }

    override fun callBackEventMain(vyloView: VyloView) {
        viewModelEvents.eventVyloView(vyloView.globalId!!, vyloView.percent!!)
    }

    private fun sendEvent(feedItem: ProfileNewsItem?) {
        feedItem?.let {
            if (feedItem.isUserNews!! && !feedItem.checkResponse())
                sendAnalyticEvent(GoogleAnalytics.NEWS, GoogleAnalytics.NEWS_PUBLISHER)
            else if (!feedItem.isUserNews!! && !feedItem.checkResponse())
                sendAnalyticEvent(GoogleAnalytics.NEWS, GoogleAnalytics.NEWS_ORIGINAL)
            else sendAnalyticEvent(GoogleAnalytics.NEWS, GoogleAnalytics.NEWS_RESPONSE)
        }
    }
}


