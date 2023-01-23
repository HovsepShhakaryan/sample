package com.vylo.main.followmain.followingfragment.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.util.GoogleAnalytics
import com.vylo.common.util.ScrollBottomListener
import com.vylo.common.util.enums.FollowingScreenType
import com.vylo.main.R
import com.vylo.main.databinding.FragmentSearchCategoryBinding
import com.vylo.main.globalsearchmain.searchcategories.presentation.adapter.CategoryAdapter
import com.vylo.main.globalsearchmain.searchcategories.presentation.viewmodel.SearchCategoriesFragmentViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchCategoryFragment : BaseFragment<FragmentSearchCategoryBinding>(),
    CategoryAdapter.AdapterCallBack {

    override fun getViewBinding() = FragmentSearchCategoryBinding.inflate(layoutInflater)
    private val viewModel by sharedViewModel<SearchCategoriesFragmentViewModel>()
    private lateinit var adapter: CategoryAdapter
    private var scrollBottomListener: ScrollBottomListener? = null
    private var isMakeSearch = true
    private var isRefreshing = false
    private var isEnableCallForData = false
    private var searchText: String? = null

    companion object {
        private lateinit var followingScreenType: FollowingScreenType

        fun newInstance(followingScreenType: FollowingScreenType): SearchCategoryFragment {
            this.followingScreenType = followingScreenType
            return SearchCategoryFragment()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        isMakeSearch = false
        isEnableCallForData = false
        viewModel.setActualData(adapter.getData())
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

        scrollBottomListener = object : ScrollBottomListener(linearLayoutManager) {
            override fun onScrolledToBottom(): Boolean {
                if (isEnableCallForData) {
                    isMakeSearch = false
                    getSearchCategoryData(searchText, false)
                    scrollBottomListener!!.isSilent(true)
                } else isEnableCallForData = true
                return false
            }
        }
        viewBinder.categoryList.addOnScrollListener(scrollBottomListener!!)

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            scrollBottomListener?.isSilent(false)
            viewBinder.swipeToRefresh.isRefreshing = true
            isMakeSearch = true
            isRefreshing = true
            isEnableCallForData = false
            getSearchCategoryData(searchText, false)
        }
        viewBinder.swipeToRefresh.setOnRefreshListener(refreshListener)

        var lastInput = ""
        var debounceJob: Job? = null
        val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        viewBinder.search.apply {
            setHint(resources.getString(R.string.label_search))
            showHideClearButton(View.VISIBLE)
            getClearButton().setOnClickListener {
                if (getInputText().isNotEmpty()) {
                    scrollBottomListener!!.isSilent(true)
                    clearSearchViewByClearButton()
                    adapter.clearData()
                }
            }
            showHideCancelButton(View.GONE)
            showHideCancelBackButton(View.VISIBLE)
            val clickOnCancelBack = View.OnClickListener {
                adapter.clearData()
                backToPrevious()
            }
            setOnClickCancelBack(clickOnCancelBack)
            initialize { text ->
                if (text != null) {
                    val newtInput = text.toString()
                    debounceJob?.cancel()
                    if (lastInput != newtInput) {
                        lastInput = newtInput
                        debounceJob = uiScope.launch {
                            delay(300)
                            if (lastInput == newtInput) {
                                scrollBottomListener!!.isSilent(true)
                                searchText = text.toString()
                                if (newtInput.isNotEmpty())
                                    if (isMakeSearch) {
                                        adapter.clearData()
                                    } else isMakeSearch = true
                                else {
                                    searchText = null
                                    isMakeSearch = true
                                }
                                getSearchCategoryData(searchText = searchText, false)
                            }
                        }
                    }
                }
            }
        }

    }

    private fun getSearchCategoryData(searchText: String?, isCategoryManipulationg: Boolean) {
        viewModel.getProfile(searchText, isCategoryManipulationg)
    }

    override fun showProgress() {
        super.showProgress()
        viewBinder.progressBar.show()
    }

    override fun hideProgress() {
        super.hideProgress()
        viewBinder.progressBar.hide()
    }

    override fun followCategory(id: Long, isFollowing: Boolean?) {
//        adapter.setFollowItem(id, isFollowing!!)
//        adapter.updateFollowStatus(id, isFollowing)
        if (isFollowing != null && isFollowing) {
            when (followingScreenType) {
                FollowingScreenType.VYLO ->
                    sendAnalyticEvent(GoogleAnalytics.FOLLOWING_VYLO, GoogleAnalytics.SEARCH)
                FollowingScreenType.NEWSSTAND ->
                    sendAnalyticEvent(GoogleAnalytics.FOLLOWING_NEWSSTAND, GoogleAnalytics.SEARCH)
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

    override fun showHide(isShow: Boolean) {}

    override fun getCategory() {}
}