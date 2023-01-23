package com.vylo.globalsearch.searchnewsstand.domain.usecase.impl

import com.vylo.globalsearch.searchnewsstand.data.repository.Mapper
import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandData
import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandItem
import com.vylo.globalsearch.searchnewsstand.domain.usecase.SearchNewsstandMapperUseCase

class SearchNewsstandMapperUseCaseImpl(
    private val mapper: Mapper
) : SearchNewsstandMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromSearchNewsstandDataToSearchNewsstandItemList(searchNewsstandData: SearchNewsstandData): List<SearchNewsstandItem>? {
        if (searchNewsstandData.next != null)
            pagingToken = searchNewsstandData.next
        else brakeCall = true
        return mapper.fromNewsstandDataToNewsstandItemList(searchNewsstandData)
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}