package com.vylo.main.newsstand.data.endpoint

import com.vylo.main.homefragment.domain.entity.response.FeedData
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsFeedApi {

    @GET(GET_FEED_PUBLISHERS_CALL)
    suspend fun getFeedPublishersCall(
        @Query("cursor") cursorToken: String?,
        @Query("category_global_id") id: String?
    ): Response<FeedData>

    @DELETE(NEWS_UPDATE)
    suspend fun deleteNews(
        @Path("global_id")
        id: String
    ): Response<Unit>

    companion object {
        private const val NEWS_UPDATE = "api/news_update/{global_id}"
    }
}