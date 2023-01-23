package com.vylo.main.insightfulfragment.data.endpoint

import com.vylo.main.insightfulfragment.domain.entity.response.InsightfulData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface InsightfulApi {

    @GET(INSIGHTFUL)
    suspend fun getInsightful(
        @Query("cursor")
        cursor: String?
    ): Response<InsightfulData>

    companion object {
        private const val INSIGHTFUL = "insights"
    }
}