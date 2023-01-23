package com.vylo.main.followmain.followfragment.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toInvisible
import com.vylo.common.ext.toVisible
import com.vylo.common.util.*
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentFollowPagerBinding
import com.vylo.main.followmain.followfragment.common.FollowType
import com.vylo.main.followmain.followfragment.presentation.adapter.FollowersAdapter
import com.vylo.main.followmain.followfragment.presentation.adapter.FollowingAdapter
import com.vylo.main.followmain.followfragment.presentation.viewmodel.FollowViewModel
import com.vylo.main.followmain.followingfragment.common.FollowingType
import com.vylo.main.profilefragment.common.ProfileData
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FollowPagerFragment : BaseFragment<FragmentFollowPagerBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by sharedViewModel<FollowViewModel>()
    override fun getViewBinding() = FragmentFollowPagerBinding.inflate(layoutInflater)
    private lateinit var scrollBottomListener: ScrollBottomListener
    private lateinit var activity: MainFlawActivity
    private var isEnableCallForData = false
    private var isAvailableFeed = true
    private var profileData: ProfileData? = null
    private var isFromProfile = true
    private var isMyProfile = true

    private val followersAdapter by lazy {
        FollowersAdapter(
            onClick = ::onFollowingClick,
            onUserClick = ::onUserClick
        )
    }
    private val followingsAdapter by lazy {
        FollowingAdapter(
            onClick = ::onFollowingClick,
            onUserClick = ::onUserClick
        )
    }

    override fun onAttach(context: Context) {
        activity = getActivity() as MainFlawActivity
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onStop() {
        super.onStop()
        viewModel.setFollowerBrakeCall(false)
        viewModel.setFollowingBrakeCall(false)
    }

    private fun beginning() {
        arguments?.let {
            profileData = it.getParcelable(USER_PROFILE)
        }
        arguments?.getBoolean(FOLLOWING_BACK_TYPE)?.let {
            isFromProfile = it
            arguments?.remove(FOLLOWING_BACK_TYPE)
        }
        arguments?.getBoolean(IS_MY_PROFILE)?.let {
            isMyProfile = it
        }
        setViewSettings(profileData?.id)
        setFollowData(profileData?.id)
    }

    private fun setViewSettings(id: String? = null) {
        viewBinder.apply {
            followList.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        R.dimen.margin_padding_size_medium_small_small_mid
                    )
                )
            )

            swipeToRefresh.setOnRefreshListener(this@FollowPagerFragment)

            val linearLayoutManager = LinearLayoutManager(context)
            scrollBottomListener = object : ScrollBottomListener(linearLayoutManager) {
                override fun onScrolledToBottom(): Boolean {
                    if (isEnableCallForData && isAvailableFeed) {
                        viewModel.getFollowers(id)
                        viewModel.getFollowings(id)
                        scrollBottomListener.isSilent(true)
                    } else {
                        isEnableCallForData = true
                    }
                    return false
                }
            }
            viewBinder.followList.addOnScrollListener(scrollBottomListener)
            viewBinder.followList.layoutManager = linearLayoutManager
        }
    }

    private fun setFollowData(id: String? = null) {
        if (viewModel.isFollowerFirstTime || viewModel.isFollowingFirstTime) {
            showProgress()
        }
        viewBinder.apply {
            arguments?.let {
                if (it.getInt(FOLLOW_TYPE) == FollowType.FOLLOWERS.ordinal) {
                    search.initialize { text ->
                        followersAdapter.filter.filter(text)
                    }
                    setFollowersData(id)
                } else {
                    search.initialize { text ->
                        followingsAdapter.filter.filter(text)
                    }
                    setFollowingData(id)
                }
            }
        }
    }

    private fun shareMyProfile() {
        showMessage("Share")
    }

    private fun setFollowersData(id: String? = null) {
        viewModel.getFollowers(id)

        viewModel.followerSuccess.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                viewBinder.apply {
                    swipeToRefresh.isEnabled = false
                    search.toGone()
                    followList.toGone()
                    focusWrapper.toGone()
                    if (isMyProfile) {
                        emptyState.apply {
                            createProfileFollowersEmptyState(::shareMyProfile)
                            toVisible()
                        }
                    }
                }
            } else {
                scrollBottomListener.isSilent(false)
                viewBinder.apply {
                    swipeToRefresh.isRefreshing = false
                    search.toVisible()
                }
                followersAdapter.submitList(it)
            }
            hideProgress()
        }

        viewModel.followerError.observe(viewLifecycleOwner) {
            isEnableCallForData = false
            isAvailableFeed = false
            scrollBottomListener.isSilent(true)
            viewBinder.swipeToRefresh.isRefreshing = false
            hideProgress()
            showMessage(it)
        }

        viewBinder.followList.adapter = followersAdapter
    }

    private fun setFollowingData(id: String? = null) {
        viewModel.getFollowings(id)

        viewModel.followingSuccess.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                viewBinder.apply {
                    swipeToRefresh.isEnabled = false
                    findUsersTitle.toGone()
                    search.toGone()
                    followList.toGone()
                    if (isMyProfile) {
                        emptyState.apply {
                            createProfileFollowingEmptyState(
                                onFindUsersToFollowClick = {
                                    navigateToFindingScreen(FollowingType.VYLO)
                                },
                                onFindPublishersToFollowClick = {
                                    navigateToFindingScreen(FollowingType.NEWSSTAND)
                                }
                            )
                            toVisible()
                        }
                    }
                }
            } else {
                scrollBottomListener.isSilent(false)
                viewBinder.apply {
                    swipeToRefresh.isRefreshing = false
                    search.toVisible()
                    findUsersTitle.toVisible()
                    findUsersTitle.setOnClickListener {
                        navigateToFindingScreen(FollowingType.VYLO)
                    }
                }
                followingsAdapter.submitList(it)
            }
            hideProgress()
        }

        viewModel.followingError.observe(viewLifecycleOwner) {
            isEnableCallForData = false
            isAvailableFeed = false
            scrollBottomListener.isSilent(true)
            viewBinder.swipeToRefresh.isRefreshing = false
            hideProgress()
            showMessage(it)
        }

        viewBinder.followList.adapter = followingsAdapter
    }

    private fun refreshData() {
        viewBinder.apply {
            search.clearSearchView()
            scrollBottomListener.isSilent(false)
            isAvailableFeed = true
            swipeToRefresh.isRefreshing = true
            viewModel.setFollowerBrakeCall(false)
            viewModel.setFollowingBrakeCall(false)
            followingsAdapter.clearData()
            followersAdapter.refreshData()
        }
    }

    private fun onFollowingClick(id: Long, isFollowing: Boolean) {
        if (isFollowing) {
            viewModel.subscribePublisher(id)
        } else {
            viewModel.unsubscribePublisher(id)
        }
    }

    private fun onUserClick(id: String?) {
        id?.let {
            navigateTo(
                when (getMainGraph()) {
                    false -> R.id.profileFragment
                    true -> R.id.mainProfileFragment
                },
                bundleOf(
                    USER_PROFILE to ProfileData(it),
                    FOLLOWING_BACK_TYPE to isFromProfile
                )
            )
        }
    }

    private fun navigateToFindingScreen(followType: FollowingType) {
        navigateTo(
            R.id.action_followFragment_to_followingSearchFragment,
            bundleOf(
                FOLLOWING_TYPE to followType,
                FOLLOWING_BACK_TYPE to isFromProfile
            )
        )
    }

    override fun showProgress() {
        super.showProgress()
        viewBinder.container.toInvisible()
        activity.showProgress()
    }

    override fun hideProgress() {
        super.hideProgress()
        viewBinder.container.toVisible()
        activity.hideProgress()
    }

    override fun onRefresh() {
        refreshData()
        setFollowData(profileData?.id)
    }
}