package com.vylo.main.navigation.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val mFragmentTitleList = mutableListOf<String>()
    private val mFragmentList = mutableListOf<Fragment>()

    val fragmentList: List<Fragment>
        get() = mFragmentList

    fun getPageTitle(position: Int): String {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

}