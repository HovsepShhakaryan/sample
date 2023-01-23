package com.vylo.main.globalsearchmain.searchvylo.domain.usecase.impl

import com.vylo.globalsearch.searchvylo.data.repository.Mapper
import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloData
import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloItem
import com.vylo.globalsearch.searchvylo.domain.usecase.SearchVyloMapperUseCase

class SearchVyloMapperUseCaseImpl(
    private val mapper: Mapper
) : SearchVyloMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromSearchVyloDataToSearchVyloItemList(searchVyloData: SearchVyloData): List<SearchVyloItem>? {
        if (searchVyloData.next != null)
            pagingToken = searchVyloData.next
        else brakeCall = true
        return mapper.fromSearchVyloDataToSearchVyloItemList(searchVyloData)
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}