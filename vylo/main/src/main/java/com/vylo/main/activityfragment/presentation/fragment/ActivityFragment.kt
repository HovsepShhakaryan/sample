package com.vylo.main.activityfragment.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.entity.VideoData
import com.vylo.common.entity.WebData
import com.vylo.common.util.*
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.activityfragment.presentation.adapter.ActivityAdapter
import com.vylo.main.activityfragment.presentation.viewmodel.ActivityViewModel
import com.vylo.main.component.events.domain.entity.request.VyloView
import com.vylo.main.component.events.presentation.EventsViewModel
import com.vylo.main.databinding.FragmentActivityBinding
import com.vylo.main.profilefragment.common.ProfileData
import com.vylo.main.videofragment.presentation.fragment.VideoFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityFragment : BaseFragment<FragmentActivityBinding>(),
    SwipeRefreshLayout.OnRefreshListener, VideoFragment.CallBackEventMain {

    private val viewModel by viewModel<ActivityViewModel>()
    private val viewModelEvents by viewModel<EventsViewModel>()
    private lateinit var activity: MainFlawActivity
    private lateinit var scrollBottomListener: ScrollBottomListener
    private var isEnableCallForData = false
    private var isAvailableFeed = true

    private val activityAdapter by lazy {
        ActivityAdapter(
            onFollowClick = ::onFollowingClick,
            onUserClick = ::onUserClick,
            onVideoClick = ::onVideoClick,
            onWebClick = ::onWebClick
        )
    }

    override fun getViewBinding() = FragmentActivityBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainFlawActivity
        activity.hideNavBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onResume() {
        super.onResume()

        setFragmentResultListener(VIDEO_NOT_ALIVE) { key, bundle ->
            bundle.getString(key).let {
                it?.let {
                    activityAdapter.removeActivity(it)
                }
            }
        }

        if (viewModel.getActualData() == null)
            getActivityData(true)
        else if (activityAdapter.getData().size == 0) {
            viewModel.getActualData()?.let {
                activityAdapter.submitList(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.setActualData(activityAdapter.getData())
    }

    private fun beginning() {
        setViewSettings()
        createToolbar()
        createContentOfView()
    }

    private fun setViewSettings() {
        viewBinder.apply {
            activityList.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        R.dimen.margin_padding_size_medium_small_small_mid
                    )
                )
            )

            swipeToRefresh.setOnRefreshListener(this@ActivityFragment)

            val layoutManager = LinearLayoutManager(context)
            scrollBottomListener = object : ScrollBottomListener(layoutManager) {
                override fun onScrolledToBottom(): Boolean {
                    if (isEnableCallForData && isAvailableFeed) {
                        getActivityData(true)
                        scrollBottomListener.isSilent(true)
                    } else {
                        isEnableCallForData = true
                    }
                    return false
                }
            }

            viewBinder.apply {
                activityList.addOnScrollListener(scrollBottomListener)
                activityList.layoutManager = layoutManager
                activityList.adapter = activityAdapter
            }
        }
    }

    private fun createToolbar() {
        viewBinder.toolbar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack { backToPrevious() }
            setTitle(resources.getString(R.string.label_activity))
            setStyleButtonBackText(com.vylo.common.R.style.MainText_H6_3)
            showBottomBorder(View.VISIBLE)
            setColorBottomBorder(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.shadow
                )
            )
            setHeightBottomBorder(com.vylo.common.R.dimen.half_one)
        }
    }

    private fun createContentOfView() {
        viewModel.activitySuccess.observe(viewLifecycleOwner) {
            activityAdapter.submitList(it)
            successResult()
        }

        viewModel.followingIds.observe(viewLifecycleOwner) {
            activityAdapter.submitFollowingIds(it)
        }

        viewModel.isEnableDataCall.observe(viewLifecycleOwner) {
            isAvailableFeed = it
        }

        viewModel.activityError.observe(viewLifecycleOwner) {
            errorResult(it)
        }

        viewModel.eventError.observe(viewLifecycleOwner) {
            it?.let { msg ->
                errorResult(msg)
            }
        }
    }

    private fun getActivityData(isShowProgressBar: Boolean) {
        scrollBottomListener.isSilent(true)
        if (isShowProgressBar) activity.showProgress()
        lifecycleScope.launch { viewModel.getActivity() }
        lifecycleScope.launch { viewModel.getFollowings() }
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
            viewModel.setActivityBrakeCall(false)
            activityAdapter.refreshData()
        }
    }

    private fun onFollowingClick(id: String, isFollowing: Boolean) {
        viewModel.followUser(id, isFollowing)
    }

    override fun onRefresh() {
        refreshData()
        getActivityData(false)
    }

    private fun onUserClick(id: String) {
        navigateTo(
            R.id.action_activityFragment2_to_mainProfileFragment,
            bundleOf(USER_PROFILE to ProfileData(id))
        )
    }

    private fun onVideoClick(data: VideoData) {
        VideoFragment.setCallBackEventMain(this)
        viewModel.viewReport(data.globalId)
        navigateTo(
            R.id.action_activityFragment2_to_videoFragment, bundleOf(
                VIDEO_DATA to data
            )
        )
    }

    private fun onWebClick(data: WebData) {
        data.globalId?.let {
            viewModel.viewReport(it)
        }
        navigateTo(R.id.action_activityFragment2_to_webFragment, bundleOf(WEB_DATA to data))
    }

    override fun onDestroy() {
        viewModel.setActivityBrakeCall(false)
        super.onDestroy()
    }

    override fun callBackEventMain(vyloView: VyloView) {
        viewModelEvents.eventVyloView(vyloView.globalId!!, vyloView.percent!!)
    }
}