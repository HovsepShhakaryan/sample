package com.vylo.main.homefragment.data.endpoint

import com.vylo.common.entity.ProfileItem
import com.vylo.main.homefragment.domain.entity.response.FeedData
import retrofit2.Response
import retrofit2.http.*

interface FeedApi {

    @GET(GET_FEEDS_CALL)
    suspend fun getFeedsCall(@Query("cursor") cursorToken: String?): Response<FeedData>

    @GET(PROFILE)
    suspend fun getMyProfile(): Response<ProfileItem>

    @DELETE(NEWS_UPDATE)
    suspend fun deleteNews(
        @Path("global_id")
        id: String
    ): Response<Unit>

    companion object {
        const val PROFILE = "api/profile"
        private const val NEWS_UPDATE = "api/news_update/{global_id}"
    }
}