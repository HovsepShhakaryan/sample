package com.vylo.main.newsstand.presentation.fragment

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vylo.common.BaseFragment
import com.vylo.common.entity.*
import com.vylo.common.ext.orFalse
import com.vylo.common.ext.shareLink
import com.vylo.common.ext.toClipboard
import com.vylo.common.ext.toVisible
import com.vylo.common.util.*
import com.vylo.common.util.enums.GraphType
import com.vylo.common.util.enums.ResponseType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.categorymain.categorytrendingfragment.presentation.adapter.CategoryAdapter
import com.vylo.main.component.adapter.FeedAdapter
import com.vylo.main.component.adapter.enum.FeedType
import com.vylo.main.component.events.domain.entity.request.VyloView
import com.vylo.main.component.events.presentation.EventsViewModel
import com.vylo.main.component.kebab.domain.entity.response.EventCounterData
import com.vylo.main.component.sharedviewmodel.ActivityFragmentSharedViewModel
import com.vylo.main.databinding.FragmentNewsStandBinding
import com.vylo.main.followmain.followingfragment.common.FollowingType
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.newsstand.presentation.viewmodel.NewsstandViewModel
import com.vylo.main.profilefragment.common.ProfileData
import com.vylo.main.profilefragment.domain.entity.response.toNewsItem
import com.vylo.main.profilefragment.presentation.fragment.DeleteDialogFragment
import com.vylo.main.videofragment.presentation.fragment.VideoFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsStandFragment : BaseFragment<FragmentNewsStandBinding>(),
    FeedAdapter.AdapterCallBack, VideoFragment.CallBackEventMain {

    private val sharedViewModel: ActivityFragmentSharedViewModel by activityViewModels()
    private val CATEGORY_SCREEN_START_POSITION = "START_POSITION_OF_CATEGORY_SCREEN"
    private val viewModel by viewModel<NewsstandViewModel>()
    private val viewModelEvents by viewModel<EventsViewModel>()
    private lateinit var scrollBottomListener: ScrollBottomListener
    private lateinit var activity: MainFlawActivity
    private var isEnableCallForData = false
    private var isAvailableFeed = true
    private val adapterFeed by lazy {
        FeedAdapter(
            adapterCallBack = this,
            context = requireContext(),
            feedType = FeedType.NEWSSTAND,
            onNewsClick = ::navigateToWebView,
            onKebabClick = ::openKebab,
            onUserClick = ::onUserClick,
            onRespondClick = ::onRespondClick,
            onResponsesClick = ::onResponsesClick
        )
    }

    private val events = mutableListOf<EventCounterData>()

    override fun getViewBinding() = FragmentNewsStandBinding.inflate(layoutInflater)

    companion object {

        @JvmStatic
        fun newInstance(): NewsStandFragment {
            return NewsStandFragment()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onStart() {
        super.onStart()
        activity.showNavBar()
    }

    override fun onResume() {
        super.onResume()
        activity.smoothContentToTop {
            viewBinder.listFeed.smoothScrollToPosition(0)
        }

        setFragmentResultListener(CATEGORY_ID) { key, bundle ->
            bundle.getParcelable<CommonCategoryItem>(key)?.let {
                viewModel.setCategoryItem(it)
                viewModel.setActualData(null)
                adapterFeed.clearData()
                viewModel.setIsActive(it.isFollow)
                viewModel.setCategoryId(it.id)
                viewBinder.categoryTitle.text = it.name
                viewBinder.imgStare.visibility = View.VISIBLE
                showHideStare(it.globalId)
            }
        }

        viewBinder.imgStare.setOnClickListener {
            if (viewModel.getCategoryId() != null)
                when (viewModel.getIsActive()) {
                    true -> {
                        viewModel.deleteCategory(viewModel.getCategoryId()!!)
                        viewBinder.imgStare.setBackgroundResource(R.drawable.ic_star_not_active)
                        viewModel.setIsActive(false)
                    }
                    false -> {
                        viewModel.addCategory(viewModel.getCategoryId()!!)
                        viewBinder.imgStare.setBackgroundResource(R.drawable.ic_star_active)
                        viewModel.setIsActive(true)
                    }
                    else -> {}
                }
        }
        when (viewModel.getIsActive()) {
            true -> viewBinder.imgStare.setBackgroundResource(R.drawable.ic_star_active)
            false -> viewBinder.imgStare.setBackgroundResource(R.drawable.ic_star_not_active)
            else -> viewBinder.imgStare.visibility = View.GONE
        }

        viewBinder.categoryTitle.text = viewModel.getCategoryItem()?.name ?: run {
            resources.getString(R.string.title_categories)
        }

        if (viewModel.getActualData() == null) {
            viewModel.setBrakeCall(false)
            getFeedData(true)
        } else if (adapterFeed.getData().size == 0) {
            viewModel.getActualData()?.let {
                if (it.isEmpty()) {
                    viewBinder.emptyState.toVisible()
                    viewBinder.emptyState.createNewsstandEmptyState(
                        onFindPublishersToFollowClick = {
                            navigateToFindingScreen(FollowingType.NEWSSTAND)
                        },
                        onFindCategoriesToFollowClick = {
                            navigateToFindingCategoryScreen(FollowingType.NEWSSTAND)
                        }
                    )
                } else {
                    adapterFeed.setData(it)
                    it.forEach {
                        it.globalId?.let { id ->
                            viewModel.getEventCounter(id)
                        }
                    }
                }
            }
        } else {
            adapterFeed.getData().let {
                it.forEach {
                    it.globalId?.let { id ->
                        viewModel.getEventCounter(id)
                    }
                }
            }
        }
        activity.showNavBar()
        beginning()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setActualData(adapterFeed.getData())
        if (adapterFeed.getTimingList().isNotEmpty()) {
            val globalIdList = mutableListOf<String>()
            for (item in adapterFeed.getTimingList()) {
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
    }

    private fun showHideStare(id: String?) {
        when (id) {
            resources.getString(R.string.title_categories),
            null -> viewBinder.imgStare.visibility = View.GONE
        }
    }

    private fun beginning() {
        createToolBar()
        createContentOfView()
    }

    private fun createToolBar() {
        activity = getActivity() as MainFlawActivity
        viewBinder.mainToolbar.showBottomBorder(View.VISIBLE)
        viewBinder.mainToolbar.setColorBottomBorder(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey
            )
        )
        val typeface =
            ResourcesCompat.getFont(requireContext(), com.vylo.common.R.font.suisse_lntl_medium)
        viewBinder.mainToolbar.setTitleFontFamily(typeface)
//        viewBinder.mainToolbar.setIconOfButtonMenu(R.drawable.ic_following)
        viewBinder.mainToolbar.setTitle(resources.getString(R.string.label_newsstand))
        viewBinder.mainToolbar.setTitleStyle(com.vylo.common.R.style.MainText_H3)
//        val buttonMenu = View.OnClickListener {
//            navigateTo(R.id.action_newsStandFragment_to_followingFragment, Bundle().apply {
//                putInt(
//                    FOLLOWING_TYPE, FollowingType.NEWSSTAND.ordinal
//                )
//            })
//        }

//        viewBinder.mainToolbar.clickOnButtonMenu(buttonMenu)

        viewBinder.mainToolbar.setIconOfButtonBack(R.drawable.ic_search)
        val buttonSearch = View.OnClickListener {
            navigateTo(
                R.id.action_newsStandFragment_to_searchStartFragment,
                bundleOf(GRAPH_ID to GraphType.NEWSTAND_GRAPH.type)
            )
        }
        viewBinder.mainToolbar.clickOnButtonBack(buttonSearch)

        viewBinder.imgExpand.setOnClickListener {
            navigateTo(
                R.id.action_newsStandFragment_to_categoryTrendingFragment,
                bundleOf(CATEGORY_SCREEN_START_POSITION to true)
            )
        }

    }

    private fun createContentOfView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        scrollBottomListener = object : ScrollBottomListener(linearLayoutManager) {
            override fun onScrolledToBottom(): Boolean {
                if (isEnableCallForData && isAvailableFeed) {
                    getFeedData(true)
                    scrollBottomListener.isSilent(true)
                } else isEnableCallForData = true
                return false
            }
        }

        viewBinder.listFeed.addOnScrollListener(scrollBottomListener)
        viewBinder.listFeed.layoutManager = linearLayoutManager
        ContextCompat.getDrawable(requireContext(), R.drawable.shape_feed_divider)?.let {
            val itemDecor = DividerItemDecoration(context, HORIZONTAL)
            itemDecor.setDrawable(it)
            viewBinder.listFeed.addItemDecoration(itemDecor)
        }
        viewBinder.listFeed.adapter = adapterFeed
        adapterFeed.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        viewModel.responseError.observe(viewLifecycleOwner) {
            isAvailableFeed = false
            isEnableCallForData = false
            scrollBottomListener.isSilent(true)
            viewBinder.swipeToRefresh.isRefreshing = false
            activity.hideProgress()

            if (it.isEmpty()) {
                viewBinder.emptyState.toVisible()
                viewBinder.emptyState.createNewsstandEmptyState(
                    onFindPublishersToFollowClick = {
                        navigateToFindingScreen(FollowingType.NEWSSTAND)
                    },
                    onFindCategoriesToFollowClick = {
                        navigateToFindingCategoryScreen(FollowingType.NEWSSTAND)
                    }
                )
            } else {
                showMessage(it)
            }
        }

        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                if (viewModel.getCategoryId() == null)
                    viewBinder.emptyState.toVisible()
                viewBinder.emptyState.createNewsstandEmptyState(
                    onFindPublishersToFollowClick = {
                        navigateToFindingScreen(FollowingType.NEWSSTAND)
                    },
                    onFindCategoriesToFollowClick = {
                        navigateToFindingCategoryScreen(FollowingType.NEWSSTAND)
                    }
                )
            } else {
                getDeepLinkData()

                scrollBottomListener.isSilent(false)
                adapterFeed.setData(it)

                it.forEach {
                    it.globalId?.let { id ->
                        viewModel.getEventCounter(id)
                    }
                }
            }
            viewBinder.swipeToRefresh.isRefreshing = false
            activity.hideProgress()
        }

        viewModel.eventSuccess.observe(viewLifecycleOwner) {
            events.removeIf { event ->
                event.globalId == it.globalId
            }
            events.add(it)
        }

        viewModel.deleteResponseSuccess.observe(viewLifecycleOwner) {
            adapterFeed.deleteData(it)
            showMessage(resources.getString(R.string.deleted_news))
        }

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            scrollBottomListener.isSilent(false)
            isAvailableFeed = true
            viewBinder.swipeToRefresh.isRefreshing = true
            viewModel.setBrakeCall(false)
            adapterFeed.clearData()
            getFeedData(false)
        }
        viewBinder.swipeToRefresh.setOnRefreshListener(refreshListener)
    }

    private fun getDeepLinkData() {
        if (sharedViewModel.getDeepLinkId() != null) {
            navigateToWebView(
                WebData(
                    globalId = sharedViewModel.getDeepLinkId()
                ),
                null
            )
            sharedViewModel.setDeepLinkId(null)
        }
    }

    private fun getFeedData(isShowProgressBar: Boolean) {
        scrollBottomListener.isSilent(true)
        if (isShowProgressBar) activity.showProgress()
        viewModel.getNewsFeed(viewModel.getCategoryItem()?.globalId)
    }

    private fun navigateToFindingScreen(followType: FollowingType) {
        navigateTo(
            R.id.action_newsStandFragment_to_followingSearchFragment,
            bundleOf(
                FOLLOWING_TYPE to followType,
                FOLLOWING_BACK_TYPE to false
            )
        )
    }

    private fun navigateToFindingCategoryScreen(followType: FollowingType) {
        navigateTo(
            R.id.action_newsStandFragment_to_followingSearchCategoryFragment2,
            bundleOf(
                FOLLOWING_TYPE to followType,
                FOLLOWING_BACK_TYPE to false
            )
        )
    }

    private fun navigateToWebView(data: WebData, feedItem: FeedItem?) {
        feedItem?.let {
            sendEvent(feedItem)
        }
        data.globalId?.let {
            viewModel.viewReport(it)
        }
        navigateTo(R.id.action_newsStandFragment_to_webFragment, bundleOf(WEB_DATA to data))
    }

    private fun openKebab(feedItem: FeedItem) {
        val isActive =
            events.find { it.globalId == feedItem.globalId }?.eventCounterItem?.iLikeIt.orFalse()
        if (viewModel.myGlobalId == feedItem.publisherId) {
            KebabManager.createMyNewsKebab(
                activity = requireActivity(),
                item = feedItem.toNewsItem(),
                isActive = isActive,
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
                isActive = isActive,
                onInsightfulClick = ::onInsightfulClick,
                onShareClick = ::onShareClick,
                onCopyLinkClick = ::onCopyLinkClick,
                onReportClick = ::onReportClick,
            )
        }

        viewModel.eventError.observe(viewLifecycleOwner) {
            showMessage(it)
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

    private fun onDeleteClick(id: String) {
        DeleteDialogFragment {
            viewModel.deleteNews(id)
        }.show(requireActivity().supportFragmentManager, null)
    }

    private fun onShareClick(id: String, link: String?) {
        link?.let {
            sendAnalyticEvent(GoogleAnalytics.SHARE, GoogleAnalytics.LIKE_NEWS)
            viewModel.shareReport(id)
            context?.shareLink(it)
        }
    }

    private fun onCopyLinkClick(id: String, link: String?) {
        link?.let {
            viewModel.shareReport(id)
            context?.toClipboard(it)
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

    private fun onUserClick(id: String?) {
        id?.let {
            navigateTo(
                R.id.action_newsStandFragment_to_profileFragment,
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
        sharedViewModel.setRespondType(GoogleAnalytics.CREATE_RESPONSES)
        val titleSheet = resources.getString(R.string.label_response)
        activity.openBottomSheetDialog(id, title, categoryId, categoryName, titleSheet)
    }

    private fun onResponsesClick(data: VideoData) {
        VideoFragment.setCallBackEventMain(this)
        navigateTo(
            R.id.action_newsStandFragment_to_videoFragment, bundleOf(
                VIDEO_DATA to data
            )
        )
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

    override fun getFeed(position: Int) {

    }

    override fun callBackEventMain(vyloView: VyloView) {
        viewModelEvents.eventVyloView(vyloView.globalId!!, vyloView.percent!!)
    }

    private fun sendEvent(feedItem: FeedItem?) {
        feedItem?.let {
            if (feedItem.isUserNews!! && !feedItem.checkResponse())
                sendAnalyticEvent(GoogleAnalytics.NEWS, GoogleAnalytics.NEWS_PUBLISHER)
            else if (!feedItem.isUserNews && !feedItem.checkResponse())
                sendAnalyticEvent(GoogleAnalytics.NEWS, GoogleAnalytics.NEWS_ORIGINAL)
            else sendAnalyticEvent(GoogleAnalytics.NEWS, GoogleAnalytics.NEWS_RESPONSE)
        }
    }
}