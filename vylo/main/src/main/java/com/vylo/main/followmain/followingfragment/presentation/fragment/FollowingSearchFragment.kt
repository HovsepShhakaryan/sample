package com.vylo.main.followmain.followingfragment.presentation.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.material.tabs.TabLayoutMediator
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.FragmentAdapter
import com.vylo.common.entity.RespondData
import com.vylo.common.util.FOLLOWING_BACK_TYPE
import com.vylo.common.util.FOLLOWING_TYPE
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.common.util.enums.FollowingScreenType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentFollowingSearchBinding
import com.vylo.main.followmain.followingfragment.common.FollowingType
import com.vylo.main.followmain.followingfragment.presentation.viewmodel.FollowingViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FollowingSearchFragment : BaseFragment<FragmentFollowingSearchBinding>() {

    override fun getViewBinding() = FragmentFollowingSearchBinding.inflate(layoutInflater)
    private val viewModel by sharedViewModel<FollowingViewModel>()
    private lateinit var activity: MainFlawActivity
    private var backType: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        private var mainFlawActivity: MainFlawActivity? = null

        fun backPressHandler(mainFlawActivity: MainFlawActivity) {
            this.mainFlawActivity = mainFlawActivity
        }
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

    override fun onPause() {
        super.onPause()
        viewModel.setBrakeCall(false)
        viewModel.setSearchBrakeCall(false)
        viewModel.setUsersData(if (viewModel.getRecommendedAdapter() != null) viewModel.getRecommendedAdapter()!!.getData() else null)
        viewModel.setUsersSearchedData(if (viewModel.getSearchAdapter() != null) viewModel.getSearchAdapter()!!.getData() else null)
    }

    override fun onResume() {
        super.onResume()
        activity.hideNavBar()
    }

    private fun beginning() {
        createContentOfView()
    }

    private fun createContentOfView() {
        activity = getActivity() as MainFlawActivity
        var followingScreenType: FollowingScreenType? = null
        var isFromProfile: Boolean? = null
        if (arguments?.get(FOLLOWING_BACK_TYPE) != null)
            isFromProfile = arguments?.get(FOLLOWING_BACK_TYPE) as Boolean
        when (arguments?.get(FOLLOWING_TYPE)) {
            FollowingType.VYLO -> {
                viewBinder.firstTitle.text = resources.getString(R.string.label_find_users)
                followingScreenType = FollowingScreenType.VYLO
            }
            FollowingType.NEWSSTAND -> {
                viewBinder.firstTitle.text = resources.getString(R.string.label_find_publishers)
                followingScreenType = FollowingScreenType.NEWSSTAND
            }
        }

        if (followingScreenType != null) {
            val fragmentAdapter = FragmentAdapter(this)
            fragmentAdapter.addFragment(RecommendedSearchFragment.newInstance(followingScreenType, isFromProfile), resources.getString(R.string.label_recommended))
            fragmentAdapter.addFragment(SearchFragment.newInstance(followingScreenType, isFromProfile), resources.getString(R.string.label_search))
            viewBinder.pager.adapter = fragmentAdapter
            TabLayoutMediator(viewBinder.tabLayout, viewBinder.pager) { tab, position ->
                tab.text = fragmentAdapter.getPageTitle(position)
            }.attach()
        }

        viewBinder.buttonClose.apply {
            setText(R.string.label_close)
            setOnClickListener {
                if (viewModel.getSearchAdapter() != null)
                    viewModel.getSearchAdapter()!!.clearData()
                backToPrevious()
            }
        }
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