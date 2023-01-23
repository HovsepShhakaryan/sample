package com.vylo.main.globalsearchmain.searchcategories.presentation.fragment

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
import com.vylo.main.R
import com.vylo.main.databinding.FragmentSearchCategoriesBinding
import com.vylo.main.globalsearchmain.searchcategories.presentation.adapter.CategoryAdapter
import com.vylo.main.globalsearchmain.searchcategories.presentation.viewmodel.SearchCategoriesFragmentViewModel
import com.vylo.main.globalsearchmain.searchmain.presentation.fragment.SearchMainFragment

class SearchCategoriesFragment : BaseFragment<FragmentSearchCategoriesBinding>(),
    SearchMainFragment.Filtering,
    CategoryAdapter.AdapterCallBack {

    override fun getViewBinding() = FragmentSearchCategoriesBinding.inflate(layoutInflater)
    private lateinit var adapter: CategoryAdapter
    private lateinit var adapterSearched: CategoryAdapter
    private var searchText: String? = null

    companion object {
        private lateinit var viewModel: SearchCategoriesFragmentViewModel

        fun newInstance(viewModel: SearchCategoriesFragmentViewModel): SearchCategoriesFragment {
            Companion.viewModel = viewModel
            return SearchCategoriesFragment()
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
        adapterSearched = CategoryAdapter(
            this,
            requireContext()
        )
        viewModel.setAdapter(adapterSearched)
        viewModel.setMainAdapter(adapter)
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
            adapterSearched.clearData()
            adapterSearched.setData(savedData)
            showHide(true)
        } else {
            showProgress()
        }

        viewModel.getProfile(null, false)
        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            viewBinder.dividerLine.toVisible()
            if (it != null) {
                hideProgress()
                if (it.isNotEmpty()) adapter.clearData()
                adapter.setData(it)
                viewBinder.secondTitle.text = resources.getString(R.string.label_browse_all_categories)
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
        viewBinder.followingList.adapter = adapterSearched
        viewBinder.followingList.addItemDecoration(
            MarginItemDecoration(
                bottom = resources.getDimensionPixelSize(
                    com.vylo.common.R.dimen.margin_padding_size_medium
                )
            )
        )
        adapterSearched.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        adapter.getSecondAdapter(adapterSearched)
    }

    override fun getFilterText(text: String) {
        searchText = text
        if (!viewModel.getIsLoadData())
            viewModel.getMainAdapter()?.search(searchText!!)
    }

    override fun followCategory(id: Long, isFollowing: Boolean?) {
        isFollowing?.let { isFollowingV ->
//            adapter.setFollowItem(id, isFollowingV)
//            adapter.updateFollowStatus(id, isFollowingV)

//            adapterSearched.setFollowItem(id, isFollowingV)
//            adapterSearched.updateFollowStatus(adapter.getSubCategoriesIds(id), isFollowingV)

            if (isFollowingV) {
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
    }

    override fun showHide(isShow: Boolean) {
        if (isShow) {
            viewBinder.noResults.toGone()
            viewBinder.followingList.toVisible()
        } else {
            viewBinder.noResults.toVisible()
            viewBinder.followingList.toGone()
        }
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