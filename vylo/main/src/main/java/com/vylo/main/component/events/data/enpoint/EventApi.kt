package com.vylo.main.component.events.data.enpoint

import com.vylo.main.component.events.domain.entity.request.ViewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface EventApi {

    companion object {
        private const val EVENT_VIEW = "view"
        private const val SOURCE_VIEW = "source_view"
        private const val EVENT_VYLO_VIEW = "vylo_view/{news_global_id}/{time_of_view}"
        private const val EVENT_SHARE = "share/{news_global_id}"
        private const val EVENT_LIKE = "like/{news_global_id}"
    }

    @POST(SOURCE_VIEW)
    suspend fun eventSourceView(
        @Body viewRequest: ViewRequest
    ): Response<Unit>

    @POST(EVENT_VIEW)
    suspend fun eventView(
        @Body viewRequest: ViewRequest
    ): Response<Unit>

    @POST(EVENT_VYLO_VIEW)
    suspend fun eventVyloView(
        @Path("news_global_id")
        newsGlobalId: String,
        @Path("time_of_view")
        timeOfView: String
    ): Response<Unit>

    @POST(EVENT_SHARE)
    suspend fun eventShare(
        @Path("news_global_id")
        newsGlobalId: String,
    ): Response<Unit>

    @POST(EVENT_LIKE)
    suspend fun eventLike(
        @Path("news_global_id")
        newsGlobalId: String,
    ): Response<Unit>
}