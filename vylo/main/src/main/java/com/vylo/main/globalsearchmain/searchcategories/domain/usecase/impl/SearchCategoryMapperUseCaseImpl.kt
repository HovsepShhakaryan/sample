package com.vylo.globalsearch.searchcategories.domain.usecase.impl

import com.vylo.common.entity.ProfileItem
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.SearchCategoryData
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.SearchCategoryItem
import com.vylo.globalsearch.searchcategories.domain.usecase.SearchCategoryMapperUseCase

class SearchCategoryMapperUseCaseImpl(
    private val mapper: com.vylo.globalsearch.searchcategories.data.repository.Mapper
) : SearchCategoryMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromSearchCategoryDataToSearchCategoryItemList(searchCategoryData: SearchCategoryData): List<SearchCategoryItem>? {
        if (searchCategoryData.next != null)
            pagingToken = searchCategoryData.next
        else brakeCall = true
        return mapper.fromSearchCategoryDataToSearchCategoryItemList(searchCategoryData)
    }

    override fun fromProfileDataToCategoriesSubscrition(profileItem: ProfileItem): MutableList<SearchCategoryItem> {
        val searchCategoryItem = mutableListOf<SearchCategoryItem>()
        if (profileItem.categoriesSubscription != null) {
            for (item in profileItem.categoriesSubscription!!) {
                searchCategoryItem.add(
                    SearchCategoryItem(
                    item.id!!.toLong(),
                    item.globalId,
                    item.name,
                    item.isActive
                )
                )
            }
        }
        return searchCategoryItem
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}