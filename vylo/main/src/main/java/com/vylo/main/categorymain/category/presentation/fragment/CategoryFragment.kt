package com.vylo.main.categorymain.category.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.categorymain.category.domain.entity.CategoryItem
import com.vylo.main.categorymain.category.presentation.adapter.CategoryAdapter
import com.vylo.main.categorymain.category.presentation.viewmodel.CategoryFragmentViewModel
import com.vylo.main.component.sharedviewmodel.NavigationSharedViewModel
import com.vylo.main.databinding.FragmentCategoryBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFragment(private val pager: ViewPager2? = null) : BaseFragment<FragmentCategoryBinding>() {

    private val viewModel by viewModel<CategoryFragmentViewModel>()
    private val sharedViewModel by sharedViewModel<NavigationSharedViewModel>()
    override fun getViewBinding() = FragmentCategoryBinding.inflate(layoutInflater)
    private lateinit var activity: MainFlawActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        beginning()
    }

    private fun beginning() {
        createNavigationBar()
        getCategoryData()
    }

    private fun getCategoryData() {
        activity.showProgress()
        viewModel.getCategories()
        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            if (it != null) {
                createCategoryList(it)
                activity.hideProgress()
            }
        }
    }

    private fun createNavigationBar() {
        activity = getActivity() as MainFlawActivity
        viewBinder.navBar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            setTitle(resources.getString(R.string.title_select_categories))
            setTitleStyle(com.vylo.common.R.style.MainText_H6)
            clickOnButtonBack {
                if (pager != null) {
                    pager.currentItem = if (sharedViewModel.responseType == 2) 1 else 2
                } else {
                    backToPrevious()
                }
            }
        }
    }

    private fun createCategoryList(categories: List<CommonCategoryItem>) {
        viewBinder.apply {
            listCategory.layoutManager = LinearLayoutManager(requireContext())
            listCategory.adapter = CategoryAdapter(categories, ::getCategory)
            listCategory.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        com.vylo.common.R.dimen.margin_padding_size_medium_mid_mid
                    )
                )
            )
        }
    }

    private fun getCategory(id: String, name: String) {
        sharedViewModel.setCategoryItem(
            CategoryItem(
                id,
                name
            )
        )

        if (pager != null) {
            pager.currentItem = if (sharedViewModel.responseType == 2) 1 else 2
        } else {
            backToPrevious()
        }
    }

}