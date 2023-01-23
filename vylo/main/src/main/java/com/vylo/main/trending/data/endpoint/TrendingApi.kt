package com.vylo.main.trending.data.endpoint

import com.vylo.common.entity.ProfileItem
import com.vylo.main.homefragment.data.endpoint.FeedApi
import com.vylo.main.homefragment.domain.entity.response.FeedData
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrendingApi {

    @GET(TRENDING)
    suspend fun getTrending(
        @Query("cursor")
        cursor: String?,
        @Query("category_id")
        categoryId: String?
    ): Response<FeedData>

    @GET(FeedApi.PROFILE)
    suspend fun getMyProfile(): Response<ProfileItem>

    @DELETE(NEWS_UPDATE)
    suspend fun deleteNews(
        @Path("global_id")
        id: String
    ): Response<Unit>

    companion object {
        private const val TRENDING = "feed/trending/"
        private const val NEWS_UPDATE = "api/news_update/{global_id}"
    }
}