package com.vylo.main.trending.presentation.fragment

import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.vylo.common.util.*
import com.vylo.common.util.enums.ResponseType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.component.adapter.FeedAdapter
import com.vylo.main.component.adapter.enum.FeedType
import com.vylo.main.component.events.domain.entity.request.VyloView
import com.vylo.main.component.events.presentation.EventsViewModel
import com.vylo.main.component.kebab.domain.entity.response.EventCounterData
import com.vylo.main.component.sharedviewmodel.ActivityFragmentSharedViewModel
import com.vylo.main.component.sharedviewmodel.NavigationSharedViewModel
import com.vylo.main.databinding.FragmentTrendingBinding
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.profilefragment.common.ProfileData
import com.vylo.main.profilefragment.domain.entity.response.toNewsItem
import com.vylo.main.profilefragment.presentation.fragment.DeleteDialogFragment
import com.vylo.main.trending.presentation.viewmodel.TrendingViewModel
import com.vylo.main.videofragment.presentation.fragment.VideoFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrendingFragment : BaseFragment<FragmentTrendingBinding>(),
    SwipeRefreshLayout.OnRefreshListener, FeedAdapter.AdapterCallBack,
    VideoFragment.CallBackEventMain {

    private val activitySharedViewModel: ActivityFragmentSharedViewModel by activityViewModels()

    private val viewModel by viewModel<TrendingViewModel>()
    private val viewModelEvents by viewModel<EventsViewModel>()
    private val sharedViewModel by sharedViewModel<NavigationSharedViewModel>()
    private lateinit var scrollBottomListener: ScrollBottomListener
    private lateinit var activity: MainFlawActivity
    private var isEnableCallForData = false
    private var isAvailableFeed = true
    private val CATEGORY_SCREEN_START_POSITION = "START_POSITION_OF_CATEGORY_SCREEN"

    private val trendingAdapter by lazy {
        FeedAdapter(
            adapterCallBack = this,
            context = requireContext(),
            feedType = FeedType.VYLO,
            onVideoClick = ::navigateToVideo,
            onNewsClick = ::navigateToWebView,
            onKebabClick = ::openKebab,
            onUserClick = ::onUserClick,
            onRespondClick = ::onRespondClick,
            onResponsesClick = ::onResponsesClick
        )
    }

    private val events = mutableListOf<EventCounterData>()

    override fun getViewBinding() = FragmentTrendingBinding.inflate(layoutInflater)

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
        activity.showNavBar()
    }

    override fun onResume() {
        super.onResume()
        activity.smoothContentToTop {
            viewBinder.trendingList.smoothScrollToPosition(0)
        }

        setFragmentResultListener(CATEGORY_ID) { key, bundle ->
            bundle.getParcelable<CommonCategoryItem>(key)?.let {
                viewModel.setCategoryItem(it)
                viewModel.setActualData(null)
                trendingAdapter.clearData()
                viewModel.setIsActive(it.isFollow)
                viewModel.setCategoryId(it.id)
                viewBinder.categoryTitle.text = it.name
                viewBinder.imgStare.visibility = View.VISIBLE
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

        if (viewModel.myGlobalId.isNullOrEmpty()) {
            viewModel.getMyProfile()
        }
        if (viewModel.getActualData() == null) {
            viewModel.setTrendingBrakeCall(false)
            getTrendingData(true)
        } else if (trendingAdapter.getData().size == 0) {
            viewModel.getActualData()?.let {
                trendingAdapter.setData(it)
                it.forEach {
                    it.globalId?.let { id ->
                        viewModel.getEventCounter(id)
                    }
                }
            }
        } else {
            trendingAdapter.getData().let {
                it.forEach {
                    it.globalId?.let { id ->
                        viewModel.getEventCounter(id)
                    }
                }
            }
        }
        activity.showNavBar()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setActualData(trendingAdapter.getData())
        if (trendingAdapter.getTimingList().isNotEmpty()) {
            val globalIdList = mutableListOf<String>()
            for (item in trendingAdapter.getTimingList()) {
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

    private fun beginning() {
        setViewSettings()
        createToolbar()
        createContentOfView()
    }

    private fun setViewSettings() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        scrollBottomListener = object : ScrollBottomListener(linearLayoutManager) {
            override fun onScrolledToBottom(): Boolean {
                if (isEnableCallForData && isAvailableFeed) {
                    getTrendingData(true)
                    scrollBottomListener.isSilent(true)
                } else {
                    isEnableCallForData = true
                }
                return false
            }
        }

        viewBinder.apply {
            trendingList.addOnScrollListener(scrollBottomListener)
            trendingList.layoutManager = linearLayoutManager
            trendingList.adapter = trendingAdapter
            trendingAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            imgExpand.setOnClickListener {
                navigateTo(
                    R.id.action_trendingFragment_to_categoryTrendingFragment,
                    bundleOf(CATEGORY_SCREEN_START_POSITION to false)
                )
            }
            ContextCompat.getDrawable(requireContext(), R.drawable.shape_feed_divider)?.let {
                val itemDecor = DividerItemDecoration(context, ClipDrawable.HORIZONTAL)
                itemDecor.setDrawable(it)
                trendingList.addItemDecoration(itemDecor)
            }
            swipeToRefresh.setOnRefreshListener(this@TrendingFragment)
        }
    }

    private fun createToolbar() {
        viewBinder.toolbar.apply {
            setTitle(resources.getString(R.string.label_trending))
            setTitleStyle(com.vylo.common.R.style.MainText_H3)
            showBottomBorder(View.VISIBLE)
            setColorBottomBorder(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
        }
    }

    private fun createContentOfView() {

        viewModel.trendingError.observe(viewLifecycleOwner) {
            errorResult(it)
        }

        viewModel.isEnableDataCall.observe(viewLifecycleOwner) {
            isAvailableFeed = it
        }

        viewModel.trendingSuccess.observe(viewLifecycleOwner) {
            if (it != null) {
                trendingAdapter.setData(it)

                it.forEach {
                    it.globalId?.let { id ->
                        viewModel.getEventCounter(id)
                    }
                }
            }
            successResult()
        }

        viewModel.eventSuccess.observe(viewLifecycleOwner) {
            events.removeIf { event ->
                event.globalId == it.globalId
            }
            events.add(it)
        }

        viewModel.deleteResponseSuccess.observe(viewLifecycleOwner) {
            trendingAdapter.deleteData(it)
            showMessage(resources.getString(R.string.deleted_news))
        }

        viewModel.categoryError.observe(viewLifecycleOwner) {
            if (it != null)
                errorResult(it)
        }
    }

    private fun getTrendingData(isShowProgressBar: Boolean) {
        scrollBottomListener.isSilent(true)
        if (isShowProgressBar) activity.showProgress()
        viewModel.getTrending(viewModel.getCategoryItem()?.globalId)
    }

    private fun successResult() {
        scrollBottomListener.isSilent(false)
        viewBinder.swipeToRefresh.isRefreshing = false
        activity.hideProgress()
    }

    private fun errorResult(errorMsg: String) {
        isEnableCallForData = false
        isAvailableFeed = false
        scrollBottomListener.isSilent(true)
        viewBinder.swipeToRefresh.isRefreshing = false
        activity.hideProgress()
        showMessage(errorMsg)
    }

    private fun refreshData() {
        viewBinder.apply {
            scrollBottomListener.isSilent(false)
            isAvailableFeed = true
            swipeToRefresh.isRefreshing = true
            viewModel.setTrendingBrakeCall(false)
            trendingAdapter.clearData()
            getTrendingData(false)
        }
    }

    private fun navigateToVideo(data: VideoData, feedItem: FeedItem?) {
        feedItem?.let {
            sendEvent(feedItem)
        }
        VideoFragment.setCallBackEventMain(this)
        data.globalId?.let {
            viewModel.viewReport(it)
        }
        navigateTo(R.id.action_trendingFragment_to_videoFragment, bundleOf(VIDEO_DATA to data))
    }

    private fun navigateToWebView(data: WebData, feedItem: FeedItem?) {
        feedItem?.let {
            sendEvent(feedItem)
        }
        data.globalId?.let {
            viewModel.viewReport(it)
        }
        navigateTo(R.id.action_trendingFragment_to_webFragment, bundleOf(WEB_DATA to data))
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
            navigateTo(R.id.profileFragment, bundleOf(USER_PROFILE to ProfileData(it)))
        }
    }

    private fun onRespondClick(
        id: String?,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
        activitySharedViewModel.setRespondType(GoogleAnalytics.CREATE_RESPONSES)
        val titleSheet = resources.getString(R.string.label_response)
        activity.openBottomSheetDialog(id, title, categoryId, categoryName, titleSheet)
    }

    private fun onResponsesClick(data: VideoData) {
        VideoFragment.setCallBackEventMain(this)
        navigateTo(
            R.id.action_trendingFragment_to_videoFragment, bundleOf(
                VIDEO_DATA to data
            )
        )
    }

    override fun onRefresh() {
        refreshData()
        createContentOfView()
    }

    override fun onDestroy() {
//        viewModel.setTrendingBrakeCall(false)
        super.onDestroy()
    }

    override fun getFeed(position: Int) {

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