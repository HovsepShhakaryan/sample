package com.vylo.main.followmain.followingfragment.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vylo.common.BaseFragment
import com.vylo.common.entity.PublishersSubscription
import com.vylo.common.util.FOLLOWING_BACK_TYPE
import com.vylo.common.util.GoogleAnalytics
import com.vylo.common.util.ScrollBottomListener
import com.vylo.common.util.USER_PROFILE
import com.vylo.common.util.enums.FollowingScreenType
import com.vylo.common.util.enums.RecommendedScreenType
import com.vylo.main.R
import com.vylo.main.databinding.FragmentRecommendedSearchBinding
import com.vylo.main.followmain.followingfragment.presentation.adapter.ProfileFollowAdapter
import com.vylo.main.followmain.followingfragment.presentation.viewmodel.FollowingViewModel
import com.vylo.main.profilefragment.common.ProfileData
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RecommendedSearchFragment : BaseFragment<FragmentRecommendedSearchBinding>(),
    ProfileFollowAdapter.AdapterCallBack, ProfileFollowAdapter.NavigateToProfile {

    override fun getViewBinding() = FragmentRecommendedSearchBinding.inflate(layoutInflater)
    private lateinit var adapter: ProfileFollowAdapter
    private val viewModel by sharedViewModel<FollowingViewModel>()
    private var scrollBottomListener: ScrollBottomListener? = null
    private var isRefreshing = false
    private var isEnableCallForData = false
    private var removedProfiles = listOf<PublishersSubscription>()

    companion object {
        private lateinit var followingScreenType: FollowingScreenType
        private var isFromProfile: Boolean? = null

        fun newInstance(followingScreenType: FollowingScreenType, isFromProfile: Boolean?): RecommendedSearchFragment {
            this.followingScreenType = followingScreenType
            this.isFromProfile = isFromProfile
            return RecommendedSearchFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ProfileFollowAdapter(
            this,
            this,
            requireContext()
        )
        viewModel.setRecommendedAdapter(adapter)
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

    override fun onResume() {
        super.onResume()
        viewModel.setBrakeCall(false)
        viewModel.setScreenType(RecommendedScreenType.RECOMMENDED)
    }

    override fun onPause() {
        super.onPause()
        isEnableCallForData = false
    }

    private fun beginning() {
        createContentOfView()
    }

    private fun createContentOfView() {
        if (viewModel.getUsersData().isEmpty())
            getSearchProfileData(true)
        else {
            adapter.clearData()
            adapter.setData(viewModel.getUsersData())
            viewModel.getUsersData().clear()
        }

        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinder.recommendedList.layoutManager = linearLayoutManager
        viewBinder.recommendedList.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        val data = mutableListOf<PublishersSubscription>()
        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                data.addAll(it)
                if (removedProfiles.isNotEmpty()) {
                    for ((index, item1) in it.withIndex()) {
                        for (item2 in removedProfiles)
                            if (item1.id == item2.id) {
                                data[index] = item1.copy(
                                    isFollow = false
                                )
                                break
                            }
                    }
                }
                if (isRefreshing) {
                    adapter.clearData()
                    isRefreshing = false
                }

                adapter.setData(data)
                data.clear()
            } else adapter.clearData()

            hideProgress()
            scrollBottomListener!!.isSilent(false)
            viewBinder.swipeToRefresh.isRefreshing = false
        }

        viewModel.responseError.observe(viewLifecycleOwner) {
            hideProgress()
            viewBinder.swipeToRefresh.isRefreshing = false
            if (it != null) showMessage(it)
        }

        scrollBottomListener = object : ScrollBottomListener(linearLayoutManager) {
            override fun onScrolledToBottom(): Boolean {
                getSearchProfileData(isShowProgressBar = true)
                scrollBottomListener!!.isSilent(true)
                return false
            }
        }
        viewBinder.recommendedList.addOnScrollListener(scrollBottomListener!!)

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            scrollBottomListener?.isSilent(false)
            viewBinder.swipeToRefresh.isRefreshing = true
            viewModel.setBrakeCall(false)
            isRefreshing = true
            getSearchProfileData(isShowProgressBar = true)
        }
        viewBinder.swipeToRefresh.setOnRefreshListener(refreshListener)

        viewModel.userAllPublishersSuccess.observe(viewLifecycleOwner) {
            if (it != null) {
                removedProfiles = it
                getSearchProfileData(isShowProgressBar = true)
                scrollBottomListener!!.isSilent(true)
            }
        }
    }

    private fun getSearchProfileData(isShowProgressBar: Boolean) {
        if (isShowProgressBar) showProgress()
        when (followingScreenType) {
            FollowingScreenType.VYLO ->
                viewModel.getProfile(true, null, false, followingScreenType, false)
            FollowingScreenType.NEWSSTAND ->
                viewModel.getProfile(true, null, false, followingScreenType, false)
        }
    }

    override fun followCategory(id: Long, isFollowing: Boolean?) {
        if (isFollowing != null && isFollowing) {
            when (followingScreenType) {
                FollowingScreenType.VYLO ->
                    sendAnalyticEvent(GoogleAnalytics.FOLLOWING_VYLO, GoogleAnalytics.RECOMENDED)
                FollowingScreenType.NEWSSTAND ->
                    sendAnalyticEvent(GoogleAnalytics.FOLLOWING_NEWSSTAND, GoogleAnalytics.RECOMENDED)
            }
            viewModel.addPublisher(id)
        } else viewModel.deletePublisher(id)
    }

    override fun navigateToProfile(id: String?) {
        id?.let {
            navigateTo(
                when (getMainGraph()) {
                    false -> R.id.action_followingSearchFragment_to_profileFragment
                    true -> R.id.action_followingSearchFragment_to_mainProfileFragment
                },
                bundleOf(
                    USER_PROFILE to ProfileData(it),
                    FOLLOWING_BACK_TYPE to isFromProfile
                )
            )
        }
    }

    override fun showProgress() {
        super.showProgress()
        viewBinder.progressBar.show()
    }

    override fun hideProgress() {
        super.hideProgress()
        viewBinder.progressBar.hide()
    }
}