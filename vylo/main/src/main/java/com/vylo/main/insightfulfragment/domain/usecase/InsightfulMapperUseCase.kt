package com.vylo.main.insightfulfragment.domain.usecase

import com.vylo.main.insightfulfragment.domain.entity.response.InsightfulData
import com.vylo.main.insightfulfragment.domain.entity.response.InsightfulItem

interface InsightfulMapperUseCase {
    fun fromInsightfulDataToInsightfulItemList(insightfulData: InsightfulData): List<InsightfulItem>?
    fun getPagingToken(): String?
}