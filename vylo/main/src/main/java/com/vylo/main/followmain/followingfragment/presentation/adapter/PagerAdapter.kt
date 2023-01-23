package com.vylo.main.followmain.followingfragment.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vylo.common.util.FOLLOWING_BACK_TYPE
import com.vylo.common.util.FOLLOWING_TYPE
import com.vylo.main.followmain.followingfragment.common.FollowingType
import com.vylo.main.followmain.followingfragment.presentation.fragment.FollowingPagerFragment

class PagerAdapter(
    fragment: Fragment,
    private val followingBackType: Boolean
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = FollowingType.values().size

    override fun createFragment(position: Int): Fragment {
        return FollowingPagerFragment().apply {
            arguments = Bundle().apply {
                putInt(FOLLOWING_TYPE, position)
                putBoolean(FOLLOWING_BACK_TYPE, followingBackType)
            }
        }
    }
}