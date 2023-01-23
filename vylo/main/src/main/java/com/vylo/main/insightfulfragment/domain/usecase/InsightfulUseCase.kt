package com.vylo.main.insightfulfragment.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.insightfulfragment.domain.entity.response.InsightfulData

interface InsightfulUseCase {
    suspend fun getInsightful(cursor: String?): Resource<InsightfulData>
}