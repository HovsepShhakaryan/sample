package com.vylo.main.videofragment.data.endpoint

import com.vylo.main.homefragment.domain.entity.response.FeedData
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoApi {

    @GET(FEED_COMENTS)
    suspend fun getResponses(
        @Path("global_id") id: String,
        @Query("cursor") cursor: String?
    ): Response<FeedData>

    @GET(FEED_BY_ID)
    suspend fun getFeedById(
        @Path("global_id") id: String
    ): Response<FeedItem>

    @GET(PROFILE_FEED_BY_ID)
    suspend fun getMyFeedById(
        @Path("global_id") id: String
    ): Response<FeedItem>

    @DELETE(NEWS_UPDATE)
    suspend fun deleteNews(
        @Path("global_id")
        id: String
    ): Response<Unit>

    companion object {
        private const val FEED_COMENTS = "feed/{global_id}/coments"
        private const val FEED_BY_ID = "feed/{global_id}"
        private const val NEWS_UPDATE = "api/news_update/{global_id}"
        private const val PROFILE_FEED_BY_ID = "api/my_columnnews/{global_id}"
    }
}