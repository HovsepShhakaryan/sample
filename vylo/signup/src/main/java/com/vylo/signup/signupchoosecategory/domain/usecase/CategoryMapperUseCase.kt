package com.vylo.signup.signupchoosecategory.domain.usecase

import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryData

interface CategoryMapperUseCase {
    fun fromSearchCategoryDataToSearchCategoryItemList(categoryData: CategoryData): List<com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryItem>?
    fun fromProfileDataToCategoriesSubscrition(profileItem: ProfileItem): MutableList<com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryItem>?
    fun getPagingToken(): String?
}