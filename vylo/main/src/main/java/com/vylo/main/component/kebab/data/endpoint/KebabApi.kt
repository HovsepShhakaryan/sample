package com.vylo.main.component.kebab.data.endpoint

import com.vylo.main.component.kebab.domain.entity.request.BlockUser
import com.vylo.main.component.kebab.domain.entity.request.ReportRequest
import com.vylo.main.component.kebab.domain.entity.request.ViewRequest
import com.vylo.main.component.kebab.domain.entity.response.EventCounterItem
import retrofit2.Response
import retrofit2.http.*

interface KebabApi {

    @GET(EVENT_COUNTER)
    suspend fun getEventCounter(
        @Path("news_global_id")
        id: String
    ): Response<EventCounterItem>

    @POST(LIKE)
    suspend fun addLike(
        @Path("news_global_id")
        id: String
    ): Response<Unit>

    @DELETE(LIKE)
    suspend fun deleteLike(
        @Path("news_global_id")
        id: String
    ): Response<Unit>

    @POST(REPORT)
    suspend fun sendReport(
        @Body report: ReportRequest
    ): Response<Any>

    @POST(SHARE)
    suspend fun shareReport(
        @Path("news_global_id")
        id: String
    ): Response<Unit>

    @POST(VIEW)
    suspend fun viewReport(
        @Body request: ViewRequest
    ): Response<Unit>

    @POST(BLOCK_USER)
    suspend fun blockUser(
        @Body block: BlockUser
    ): Response<Any>

    companion object {
        const val EVENT_COUNTER = "events_counter/{news_global_id}"
        const val LIKE = "like/{news_global_id}"
        const val REPORT = "api/columnnews_report"
        const val SHARE = "share/{news_global_id}"
        const val VIEW = "view"
        const val BLOCK_USER = "api/profile/block/"
    }
}