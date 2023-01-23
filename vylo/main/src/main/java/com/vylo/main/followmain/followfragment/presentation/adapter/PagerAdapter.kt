package com.vylo.main.followmain.followfragment.presentation.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vylo.common.util.FOLLOWING_BACK_TYPE
import com.vylo.common.util.FOLLOW_TYPE
import com.vylo.common.util.IS_MY_PROFILE
import com.vylo.common.util.USER_PROFILE
import com.vylo.main.followmain.followfragment.common.FollowType
import com.vylo.main.followmain.followfragment.presentation.fragment.FollowPagerFragment
import com.vylo.main.profilefragment.common.ProfileData

class PagerAdapter(
    fragment: Fragment,
    private val data: ProfileData?,
    private val isFromProfile: Boolean,
    private val isMyProfile: Boolean
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = FollowType.values().size

    override fun createFragment(position: Int): Fragment {
        return FollowPagerFragment().apply {
            arguments = bundleOf(
                FOLLOW_TYPE to position,
                USER_PROFILE to data,
                FOLLOWING_BACK_TYPE to isFromProfile,
                IS_MY_PROFILE to isMyProfile
            )
        }
    }
}