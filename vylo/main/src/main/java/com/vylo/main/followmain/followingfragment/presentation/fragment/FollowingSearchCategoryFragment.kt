package com.vylo.main.followmain.followingfragment.presentation.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.FragmentAdapter
import com.vylo.common.util.FOLLOWING_TYPE
import com.vylo.common.util.enums.FollowingScreenType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentFollowingSearchCategoryBinding
import com.vylo.main.followmain.followingfragment.common.FollowingType
import com.vylo.main.globalsearchmain.searchcategories.presentation.viewmodel.SearchCategoriesFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowingSearchCategoryFragment : BaseFragment<FragmentFollowingSearchCategoryBinding>() {

    override fun getViewBinding() = FragmentFollowingSearchCategoryBinding.inflate(layoutInflater)
    private lateinit var activity: MainFlawActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        private var mainFlawActivity: MainFlawActivity? = null

        fun backPressHandler(mainFlawActivity: MainFlawActivity) {
            this.mainFlawActivity = mainFlawActivity
        }
    }

    override fun onResume() {
        super.onResume()
        activity.hideNavBar()
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

    private fun beginning() {
        createContentOfView()
    }

    private fun createContentOfView() {
        activity = getActivity() as MainFlawActivity
        val viewModelCategory by viewModel<SearchCategoriesFragmentViewModel>()

        var followingScreenType: FollowingScreenType? = null
        when (arguments?.get(FOLLOWING_TYPE)) {
            FollowingType.VYLO -> {
                viewBinder.firstTitle.text = resources.getString(R.string.label_find_category)
                followingScreenType = FollowingScreenType.VYLO
            }
            FollowingType.NEWSSTAND -> {
                viewBinder.firstTitle.text = resources.getString(R.string.label_find_category)
                followingScreenType = FollowingScreenType.NEWSSTAND
            }
        }

        if (followingScreenType != null) {
            val fragmentAdapter = FragmentAdapter(this)
            fragmentAdapter.addFragment(RecommenderSearchCategoriesFragment.newInstance(viewModelCategory, followingScreenType), resources.getString(R.string.label_recommended))
            fragmentAdapter.addFragment(SearchCategoryFragment.newInstance(followingScreenType), resources.getString(R.string.label_search))
            viewBinder.pager.adapter = fragmentAdapter
            TabLayoutMediator(viewBinder.tabLayout, viewBinder.pager) { tab, position ->
                tab.text = fragmentAdapter.getPageTitle(position)
            }.attach()
        }

        viewBinder.buttonClose.apply {
            setText(R.string.label_close)
            setOnClickListener {
                backToPrevious()
            }
        }
    }
}