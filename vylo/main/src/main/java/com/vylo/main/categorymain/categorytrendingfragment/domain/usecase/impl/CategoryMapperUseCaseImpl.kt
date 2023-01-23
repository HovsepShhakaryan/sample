package com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.impl

import com.vylo.main.categorymain.categorytrendingfragment.data.Mapper
import com.vylo.main.categorymain.categorytrendingfragment.domain.entity.response.CategoryItem
import com.vylo.main.categorymain.categorytrendingfragment.domain.entity.response.SearchCategoryData
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.CategoryMapperUseCase

class CategoryMapperUseCaseImpl(
    private val mapper: Mapper
) : CategoryMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromSearchCategoryDataToCategoryItemList(categoryData: SearchCategoryData): List<CategoryItem>? {
        if (categoryData.next != null)
            pagingToken = categoryData.next
        else brakeCall = true
        return mapper.fromSearchCategoryDataToCategoryItemList(categoryData)
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}