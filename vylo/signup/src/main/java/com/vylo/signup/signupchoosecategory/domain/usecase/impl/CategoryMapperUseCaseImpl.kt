package com.vylo.signup.signupchoosecategory.domain.usecase.impl

import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupchoosecategory.data.repository.Mapper
import com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryData
import com.vylo.signup.signupchoosecategory.domain.usecase.CategoryMapperUseCase

class CategoryMapperUseCaseImpl(
    private val mapper: Mapper
) : CategoryMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromSearchCategoryDataToSearchCategoryItemList(categoryData: CategoryData): List<com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryItem>? {
        if (categoryData.next != null)
            pagingToken = categoryData.next
        else brakeCall = true
        return mapper.fromSearchCategoryDataToSearchCategoryItemList(categoryData)
    }

    override fun fromProfileDataToCategoriesSubscrition(profileItem: ProfileItem): MutableList<com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryItem> {
        val categoryItem = mutableListOf<com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryItem>()
        if (profileItem.categoriesSubscription != null) {
            for (item in profileItem.categoriesSubscription!!) {
                categoryItem.add(com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryItem(
                    item.id!!.toLong(),
                    item.globalId,
                    item.name,
                    item.isActive
                ))
            }
        }
        return categoryItem
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}