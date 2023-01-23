package com.vylo.main.categorymain.categorytrendingfragment.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toVisible
import com.vylo.common.util.CATEGORY_ID
import com.vylo.common.util.ScrollBottomListener
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ButtonType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.categorymain.categorytrendingfragment.presentation.adapter.CategoryAdapter
import com.vylo.main.categorymain.categorytrendingfragment.presentation.viewmodel.CategoryViewModel
import com.vylo.main.databinding.FragmentTrendingCategoryBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryTrendingFragment : BaseFragment<FragmentTrendingCategoryBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val CATEGORY_SCREEN_START_POSITION = "START_POSITION_OF_CATEGORY_SCREEN"
    private var screenStartPoint: Boolean? = null
    private val viewModel by viewModel<CategoryViewModel>()

    override fun getViewBinding() = FragmentTrendingCategoryBinding.inflate(layoutInflater)

    private lateinit var scrollBottomListener: ScrollBottomListener
    private lateinit var activity: MainFlawActivity
    private var isEnableCallForData = false
    private var isAvailableFeed = true

    private val categoryAdapter by lazy {
        CategoryAdapter(
            onFollowClick = ::onFollowingClick,
            onCategoryClick = ::onCategoryClick
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainFlawActivity
        activity.hideNavBar()
        activity.showProgress()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
        createToolbar()
        setViewSettings()
        createContentOfView()
    }

    private fun createToolbar() {
        viewBinder.toolbar.apply {
            setTitleStyle(com.vylo.common.R.style.MainText_H3)
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack {
                backToPrevious()
            }
        }
        viewBinder.mainTitle.text = resources.getString(R.string.view_by_category)
    }

    private fun setViewSettings() {
        viewBinder.apply {
            categoryList.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        R.dimen.margin_padding_size_medium_small_small_mid
                    )
                )
            )

            scrollBottomListener = object : ScrollBottomListener(LinearLayoutManager(context)) {
                override fun onScrolledToBottom(): Boolean {
                    if (isEnableCallForData && isAvailableFeed) {
                        viewModel.getCategory(null)
                        scrollBottomListener.isSilent(true)
                    } else {
                        isEnableCallForData = true
                    }
                    return false
                }

            }
        }
    }

    private fun createContentOfView() {
        screenStartPoint = arguments?.getBoolean(CATEGORY_SCREEN_START_POSITION)
        when (screenStartPoint) {
            true -> viewBinder.toolbar.setTitle(resources.getString(R.string.label_newsstand))
            false -> viewBinder.toolbar.setTitle(resources.getString(R.string.label_trending))
            else -> {}
        }
        lifecycleScope.launch {
            viewModel.getCategory(null)
            viewModel.getProfile()
        }

        viewModel.categoryError.observe(viewLifecycleOwner) {
            if (it != null)
                errorResult(it)
        }

        viewModel.myCategoryError.observe(viewLifecycleOwner) {
            if (it != null)
                errorResult(it)
        }

        viewModel.categorySuccess.observe(viewLifecycleOwner) { categories ->
            if (categories != null)
                viewModel.myCategorySuccess.observe(viewLifecycleOwner) { myCategories ->
                    if (myCategories != null) {
                        val myCategoriesId = myCategories.map { it.globalId }
                        val categoryList = categories.map {
                            it.copy(isFollow = myCategoriesId.contains(it.globalId))
                        }
                        categoryAdapter.submitList(categoryList)
                        when (screenStartPoint) {
                            true -> {
                                viewBinder.close.toVisible()
                                viewBinder.buttonActions.toVisible()
                            }
                            false -> {
                                viewBinder.close.toGone()
                                viewBinder.buttonActions.toGone()
                            }
                            else -> {}
                        }

                    }
                }
            successResult()
        }

        viewBinder.buttonViewAll.apply {
            roundedWhiteButtonStyle(
                requireContext(),
                resources.getString(R.string.label_view_all_favorites),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            clickOnButton {
                CommonCategoryItem(
                    globalId = null,
                    name = resources.getString(R.string.title_favorites)
                ).let {
                    onCategoryClick(it)
                }
            }
        }

        viewBinder.buttonSeeAllCategories.apply {
            setButtonShape(ButtonType.BUTTON_GRAY_ROUND)
            setTitle(resources.getString(R.string.label_see_all_categories))
            setButtonColor(ContextCompat.getColor(requireContext(), R.color.main))
            clickOnButton {
                CommonCategoryItem(
                    globalId = resources.getString(R.string.title_categories),
                    name = resources.getString(R.string.title_categories)
                ).let {
                    onCategoryClick(it)
                }
            }
        }

        viewBinder.categoryList.layoutManager = LinearLayoutManager(requireContext())
        viewBinder.categoryList.adapter = categoryAdapter

        viewBinder.close.setOnClickListener { backToPrevious() }
    }

    private fun successResult() {
        scrollBottomListener.isSilent(false)
        activity.hideProgress()
    }

    private fun errorResult(errorMsg: String) {
        isEnableCallForData = false
        isAvailableFeed = false
        scrollBottomListener.isSilent(true)
        activity.hideProgress()
        showMessage(errorMsg)
    }

    private fun refreshData() {
        viewBinder.apply {
            scrollBottomListener.isSilent(false)
            isAvailableFeed = true
            viewModel.setCategoryBrakeCall(false)
            categoryAdapter.refreshData()
        }
    }

    private fun onFollowingClick(id: Long, isFollowing: Boolean) {
        categoryAdapter.setFollowItem(id, isFollowing)
//        categoryAdapter.updateFollowStatus(id, isFollowing)
        if (isFollowing) {
            viewModel.addCategory(id)
            categoryAdapter.getSubCategoriesIds(id)?.map { subId ->
                subId?.let {
                    viewModel.addCategory(it)
                }
            }
        } else {
            viewModel.deleteCategory(id)
            categoryAdapter.getSubCategoriesIds(id)?.map { subId ->
                subId?.let {
                    viewModel.deleteCategory(it)
                }
            }
        }
    }

    private fun onCategoryClick(categoryItem: CommonCategoryItem) {
        setFragmentResult(CATEGORY_ID, bundleOf(CATEGORY_ID to categoryItem))
        backToPrevious()
    }

    override fun onRefresh() {
        refreshData()
        createContentOfView()
    }

    override fun onDestroy() {
        viewModel.setCategoryBrakeCall(false)
        activity.showNavBar()
        super.onDestroy()
    }
}