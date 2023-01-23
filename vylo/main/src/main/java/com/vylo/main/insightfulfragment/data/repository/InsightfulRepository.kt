package com.vylo.main.insightfulfragment.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.insightfulfragment.domain.entity.response.InsightfulData

interface InsightfulRepository {
    suspend fun getInsightful(cursor: String?): Resource<InsightfulData>
}