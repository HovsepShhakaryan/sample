package com.vylo.main.followmain.followingfragment.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import com.google.android.material.tabs.TabLayoutMediator
import com.vylo.common.BaseFragment
import com.vylo.common.entity.RespondData
import com.vylo.common.util.FOLLOWING_BACK_TYPE
import com.vylo.common.util.FOLLOWING_TYPE
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentFollowingBinding
import com.vylo.main.followmain.followingfragment.common.FollowingType
import com.vylo.main.followmain.followingfragment.presentation.adapter.PagerAdapter
import com.vylo.main.followmain.followingfragment.presentation.viewmodel.FollowingViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FollowingFragment : BaseFragment<FragmentFollowingBinding>() {

    private val viewModel by sharedViewModel<FollowingViewModel>()
    override fun getViewBinding() = FragmentFollowingBinding.inflate(layoutInflater)
    private lateinit var activity: MainFlawActivity
    private var isFromProfile = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onPause() {
        super.onPause()
        viewModel.setUsers(if (viewModel.getUsersAdapter() != null) viewModel.getUsersAdapter()!!.getData() else null)
    }

    private fun beginning() {
        createToolbar()
        createTabAndPager()
    }

    override fun onResume() {
        super.onResume()
        activity.showNavBar()
    }

    private fun createToolbar() {
        activity = getActivity() as MainFlawActivity
        viewBinder.toolbar.apply {
            val typeface =
                ResourcesCompat.getFont(requireContext(), com.vylo.common.R.font.suisse_lntl_medium)
            setTitleFontFamily(typeface)
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack {
                backToPrevious()
            }
            setTitle(resources.getString(R.string.label_page_following))
            setTitleStyle(com.vylo.common.R.style.MainText_H3)
            setStyleButtonBackText(com.vylo.common.R.style.MainText_H4)
        }
    }

    private fun backNavigate() {
        arguments?.let {
            when (it.getInt(FOLLOWING_TYPE)) {
                FollowingType.VYLO.ordinal -> navigateTo(R.id.action_followingFragment_to_homeFragment)
                FollowingType.NEWSSTAND.ordinal -> navigateTo(R.id.action_followingFragment_to_newsStandFragment)
            }
        }
    }

    private fun createTabAndPager() {
        arguments?.getBoolean(FOLLOWING_BACK_TYPE)?.let {
            isFromProfile = it
            arguments?.remove(FOLLOWING_BACK_TYPE)
        }
        viewBinder.apply {
            pager.adapter = PagerAdapter(this@FollowingFragment, isFromProfile)

            setTabTitle()
            val followingType = arguments?.getInt(FOLLOWING_TYPE) ?: 0
            pager.setCurrentItem(followingType, false)
        }
    }

    private fun setTabTitle() {
        TabLayoutMediator(viewBinder.tabLayout, viewBinder.pager) { tab, position ->
            val type = FollowingType.values()[position]
            tab.text = when (type) {
                FollowingType.VYLO -> type.typeName
                FollowingType.NEWSSTAND -> type.typeName
            }
        }.attach()
    }

    override fun showProgress() {
        super.showProgress()
        viewBinder.progressBar.show()
    }

    override fun hideProgress() {
        super.hideProgress()
        viewBinder.progressBar.hide()
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
}