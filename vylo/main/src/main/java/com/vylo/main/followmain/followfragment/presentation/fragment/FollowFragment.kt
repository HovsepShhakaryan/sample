package com.vylo.main.followmain.followfragment.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.vylo.common.BaseFragment
import com.vylo.common.ext.orFalse
import com.vylo.common.ext.orZero
import com.vylo.common.util.FOLLOWING_BACK_TYPE
import com.vylo.common.util.FOLLOW_TYPE
import com.vylo.common.util.FOLLOW_USER_DATA
import com.vylo.common.util.USER_PROFILE
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentFollowBinding
import com.vylo.main.followmain.followfragment.common.FollowType
import com.vylo.main.followmain.followfragment.common.FollowUserData
import com.vylo.main.followmain.followfragment.presentation.adapter.PagerAdapter
import com.vylo.main.followmain.followfragment.presentation.viewmodel.FollowViewModel
import com.vylo.main.followmain.followingfragment.presentation.viewmodel.FollowingViewModel
import com.vylo.main.profilefragment.common.ProfileData
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FollowFragment : BaseFragment<FragmentFollowBinding>() {

    private val viewModel by sharedViewModel<FollowViewModel>()
    private val viewModelFollowing by sharedViewModel<FollowingViewModel>()
    override fun getViewBinding() = FragmentFollowBinding.inflate(layoutInflater)

    private lateinit var activity: MainFlawActivity
    private var userProfile: ProfileData? = null
    private var isFromProfile = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onAttach(context: Context) {
        activity = getActivity() as MainFlawActivity
        activity.hideNavBar()
        activity.showProgress()
        super.onAttach(context)
    }

    private fun beginning() {
        arguments?.getBoolean(FOLLOWING_BACK_TYPE)?.let {
            isFromProfile = it
            arguments?.remove(FOLLOWING_BACK_TYPE)
        }
        arguments?.getParcelable<FollowUserData>(FOLLOW_USER_DATA).let {
            createToolbar(it?.getUserNameValue().orEmpty(), it?.isApproved ?: false)
            createTabAndPager(
                it?.followersCounter ?: 0,
                it?.followingCounter ?: 0,
                it?.isMyProfile.orFalse()
            )
        }
        createContentOfView()
        viewModelFollowing.setUsersSearchedData(null)
    }

    private fun createContentOfView() {
        viewModel.publisherSuccess.observe(viewLifecycleOwner) {
            setTabTitle(it.followersCounter.orZero(), it.followingCounter.orZero())
        }

        viewModel.publisherError.observe(viewLifecycleOwner) {
            showMessage(it)
        }
    }

    private fun createToolbar(userName: String, isApproved: Boolean) {
        viewBinder.toolbar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack { backToPrevious() }
//            if (isApproved) {
//                setTitle(
//                    userName,
//                    R.drawable.ic_verified_profile,
//                    DrawablePositionType.RIGHT,
//                    R.dimen.margin_padding_size_small_mid
//                )
//            } else {
            setTitle(userName)
//            }
            setTitleStyle(com.vylo.common.R.style.MainText_H7)
        }
    }

    private fun createTabAndPager(
        followersCounter: Int,
        followingCounter: Int,
        isMyProfile: Boolean
    ) {
        viewBinder.apply {
            userProfile = arguments?.getParcelable(USER_PROFILE)
            pager.adapter = PagerAdapter(
                this@FollowFragment,
                userProfile,
                isFromProfile,
                isMyProfile
            )

            setTabTitle(followersCounter, followingCounter)
            val followType = arguments?.getInt(FOLLOW_TYPE) ?: 0
            pager.setCurrentItem(followType, false)
        }
    }

    private fun setTabTitle(followersCounter: Int, followingCounter: Int) {
        TabLayoutMediator(viewBinder.tabLayout, viewBinder.pager) { tab, position ->
            val type = FollowType.values()[position]
            tab.text = when (type) {
                FollowType.FOLLOWERS -> {
                    "$followersCounter ${type.typeName}"
                }
                FollowType.FOLLOWING -> {
                    "$followingCounter ${type.typeName}"
                }
            }
        }.attach()
    }

    override fun onDestroy() {
        viewModel.setFollowerBrakeCall(false)
        viewModel.setFollowingBrakeCall(false)
        super.onDestroy()
    }
}