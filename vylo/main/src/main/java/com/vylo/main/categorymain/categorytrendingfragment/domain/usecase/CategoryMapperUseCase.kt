package com.vylo.main.categorymain.categorytrendingfragment.domain.usecase

import com.vylo.main.categorymain.categorytrendingfragment.domain.entity.response.CategoryItem
import com.vylo.main.categorymain.categorytrendingfragment.domain.entity.response.SearchCategoryData

interface CategoryMapperUseCase {
    fun fromSearchCategoryDataToCategoryItemList(categoryData: SearchCategoryData): List<CategoryItem>?
    fun getPagingToken(): String?
}