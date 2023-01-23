package com.vylo.main.insightfulfragment.domain.usecase.impl

import com.vylo.main.insightfulfragment.domain.entity.response.InsightfulData
import com.vylo.main.insightfulfragment.domain.entity.response.InsightfulItem
import com.vylo.main.insightfulfragment.domain.usecase.InsightfulMapperUseCase

class InsightfulMapperUseCaseImpl(
    private val mapper: com.vylo.main.insightfulfragment.data.Mapper
) : InsightfulMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromInsightfulDataToInsightfulItemList(insightfulData: InsightfulData): List<InsightfulItem>? {
        if (insightfulData.next != null)
            pagingToken = insightfulData.next
        else brakeCall = true
        return mapper.fromInsightfulDataToInsightfulItemList(insightfulData)
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}