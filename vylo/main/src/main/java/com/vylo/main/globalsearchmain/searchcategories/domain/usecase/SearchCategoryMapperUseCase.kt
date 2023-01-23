package com.vylo.globalsearch.searchcategories.domain.usecase

import com.vylo.common.entity.ProfileItem
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.SearchCategoryData
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.SearchCategoryItem

interface SearchCategoryMapperUseCase {
    fun fromSearchCategoryDataToSearchCategoryItemList(searchCategoryData: SearchCategoryData): List<SearchCategoryItem>?
    fun fromProfileDataToCategoriesSubscrition(profileItem: ProfileItem): MutableList<SearchCategoryItem>?
    fun getPagingToken(): String?
}