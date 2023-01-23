package com.vylo.main.followmain.followingfragment.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toVisible
import com.vylo.common.util.GoogleAnalytics
import com.vylo.common.util.enums.FollowingScreenType
import com.vylo.main.R
import com.vylo.main.databinding.FragmentRecomendedSearchCategoriesBinding
import com.vylo.main.databinding.FragmentSearchCategoriesBinding
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.SearchCategoryItem
import com.vylo.main.globalsearchmain.searchcategories.presentation.adapter.CategoryAdapter
import com.vylo.main.globalsearchmain.searchcategories.presentation.viewmodel.SearchCategoriesFragmentViewModel
import com.vylo.main.globalsearchmain.searchmain.presentation.fragment.SearchMainFragment

class RecommenderSearchCategoriesFragment : BaseFragment<FragmentRecomendedSearchCategoriesBinding>(),
    CategoryAdapter.AdapterCallBack {

    override fun getViewBinding() = FragmentRecomendedSearchCategoriesBinding.inflate(layoutInflater)
    private lateinit var adapter: CategoryAdapter
    private var searchText: String? = null

    companion object {
        private lateinit var followingScreenType: FollowingScreenType
        private lateinit var viewModel: SearchCategoriesFragmentViewModel

        fun newInstance(viewModel: SearchCategoriesFragmentViewModel, followingScreenType: FollowingScreenType): RecommenderSearchCategoriesFragment {
            this.followingScreenType = followingScreenType
            this.viewModel = viewModel
            return RecommenderSearchCategoriesFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createAdapters()
    }

    private fun createAdapters() {
        adapter = CategoryAdapter(
            this,
            requireContext()
        )
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
        if (viewModel.getActualData() != null) {
            val savedData = mutableListOf<CommonCategoryItem>()
            savedData.addAll(viewModel.getActualData()!!)
            showHide(true)
        } else {
            showProgress()
        }

        viewModel.getProfile(null, false)
        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            if (it != null) {
                hideProgress()
                if (it.isNotEmpty()) adapter.clearData()
                adapter.setData(it)
                if (viewModel.getIsLoadData()) {
                    viewModel.setIsLoadData(false)
                    if (searchText != null && searchText!!.isNotEmpty())
                        adapter.search(searchText!!)
                }
            }
        }

        viewModel.profileError.observe(viewLifecycleOwner) {
            if (it != null) showMessage(it)
        }

        viewModel.responseSuccessActualData.observe(viewLifecycleOwner) {
            if (it != null)
                adapter.setActualData(it)
        }

        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinder.categoryList.layoutManager = linearLayoutManager
        viewBinder.categoryList.adapter = adapter
        viewBinder.categoryList.addItemDecoration(
            MarginItemDecoration(
                bottom = resources.getDimensionPixelSize(
                    com.vylo.common.R.dimen.margin_padding_size_medium
                )
            )
        )
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun followCategory(id: Long, isFollowing: Boolean?) {
//        adapter.setFollowItem(id, isFollowing!!)
//        adapter.updateFollowStatus(id, isFollowing)
        if (isFollowing != null && isFollowing) {
            when (followingScreenType) {
                FollowingScreenType.VYLO ->
                    sendAnalyticEvent(GoogleAnalytics.FOLLOWING_VYLO, GoogleAnalytics.RECOMENDED)
                FollowingScreenType.NEWSSTAND ->
                    sendAnalyticEvent(GoogleAnalytics.FOLLOWING_NEWSSTAND, GoogleAnalytics.RECOMENDED)
            }
            viewModel.addCategory(id)
            adapter.getSubCategoriesIds(id)?.map { subId ->
                subId?.let {
                    viewModel.addCategory(it)
                }
            }
        } else {
            viewModel.deleteCategory(id)
            adapter.getSubCategoriesIds(id)?.map { subId ->
                subId?.let {
                    viewModel.deleteCategory(it)
                }
            }
        }
    }

    override fun showHide(isShow: Boolean) {

    }

    override fun getCategory() {
        viewModel.insertRecent(searchText.toString())
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