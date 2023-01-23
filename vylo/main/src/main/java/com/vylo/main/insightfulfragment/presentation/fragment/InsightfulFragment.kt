package com.vylo.main.insightfulfragment.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.entity.VideoData
import com.vylo.common.util.ScrollBottomListener
import com.vylo.common.util.USER_PROFILE
import com.vylo.common.util.VIDEO_DATA
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentInsightfulBinding
import com.vylo.main.insightfulfragment.presentation.adapter.InsightfulAdapter
import com.vylo.main.insightfulfragment.presentation.viewmodel.InsightfulViewModel
import com.vylo.main.profilefragment.common.ProfileData
import org.koin.androidx.viewmodel.ext.android.viewModel

class InsightfulFragment : BaseFragment<FragmentInsightfulBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by viewModel<InsightfulViewModel>()
    private lateinit var activity: MainFlawActivity
    private lateinit var scrollBottomListener: ScrollBottomListener
    private var isEnableCallForData = false
    private var isAvailableFeed = true

    private val insightfulAdapter by lazy {
        InsightfulAdapter(
            onUserClick = ::onUserClick,
            onNewsClick = ::onNewsClick
        )
    }

    override fun getViewBinding() = FragmentInsightfulBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainFlawActivity
        activity.showProgress()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
        setViewSettings()
        createToolbar()
        createContentOfView()
    }

    private fun setViewSettings() {
        viewBinder.apply {
            insightsList.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        R.dimen.margin_padding_size_medium_small_small_mid
                    )
                )
            )

            swipeToRefresh.setOnRefreshListener(this@InsightfulFragment)

            val layoutManager = LinearLayoutManager(context)
            scrollBottomListener = object : ScrollBottomListener(LinearLayoutManager(context)) {
                override fun onScrolledToBottom(): Boolean {
                    if (isEnableCallForData && isAvailableFeed) {
                        getInsightsData(true)
                        scrollBottomListener.isSilent(true)
                    } else {
                        isEnableCallForData = true
                    }
                    return false
                }
            }

            viewBinder.apply {
                insightsList.layoutManager = layoutManager
                insightsList.adapter = insightfulAdapter
            }
        }
    }

    private fun createToolbar() {
        viewBinder.toolbar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack { backToPrevious() }
            setTitle(resources.getString(R.string.title_insightful))
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
        getInsightsData(true)

        viewModel.insightfulSuccess.observe(viewLifecycleOwner) {
            insightfulAdapter.submitList(it)
            successResult()
        }

        viewModel.insightfulError.observe(viewLifecycleOwner) {
            errorResult(it)
        }

        viewModel.eventError.observe(viewLifecycleOwner) {
            it?.let { msg ->
                errorResult(msg)
            }
        }
    }

    private fun getInsightsData(isShowProgressBar: Boolean) {
        scrollBottomListener.isSilent(true)
        if (isShowProgressBar) activity.showProgress()
        viewModel.getInsightful()
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
            viewModel.setInsightfulBrakeCall(false)
            insightfulAdapter.refreshData()
        }
    }

    override fun onRefresh() {
        refreshData()
        getInsightsData(false)
    }

    private fun onUserClick(id: String) {
        navigateTo(
            R.id.action_insightfulFragment_to_mainProfileFragment,
            bundleOf(USER_PROFILE to ProfileData(id))
        )
    }

    private fun onNewsClick(data: VideoData) {
        viewModel.viewReport(data.globalId)
        navigateTo(
            R.id.action_insightfulFragment2_to_videoFragment, bundleOf(
                VIDEO_DATA to data
            )
        )
    }

    override fun onPause() {
        super.onPause()
        viewModel.setInsightfulBrakeCall(false)
    }
}