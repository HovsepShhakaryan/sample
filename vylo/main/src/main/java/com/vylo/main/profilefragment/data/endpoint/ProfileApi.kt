package com.vylo.main.profilefragment.data.endpoint

import com.vylo.main.component.entity.ProfileItem
import com.vylo.main.component.entity.ProfilePhotoItem
import com.vylo.main.profilefragment.domain.entity.request.NewsUpdateRequest
import com.vylo.main.profilefragment.domain.entity.request.ProfileRequest
import com.vylo.main.profilefragment.domain.entity.response.NewsResponse
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsData
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsItem
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET(PROFILE)
    suspend fun getProfile(): Response<ProfileItem>

    @GET(USER_PROFILE)
    suspend fun getUserProfile(
        @Path("global_id")
        id: String
    ): Response<ProfileItem>

    @PATCH(PROFILE)
    suspend fun updateProfile(
        @Body data: ProfileRequest
    ): Response<ProfileItem>

    @Multipart
    @POST(PROFILE_IMAGE)
    suspend fun updateProfilePhoto(
        @Part body: List<MultipartBody.Part>
    ): Response<ProfilePhotoItem>

    @GET(PROFILE_NEWS)
    suspend fun getProfileNews(
        @Query("page_size")
        pageSize: Int?,
        @Query("cursor")
        cursor: String?
    ): Response<ProfileNewsData>

    @GET(USER_PROFILE_NEWS)
    suspend fun getUserProfileNews(
        @Path("publisher_id")
        id: String,
        @Query("cursor")
        cursor: String?
    ): Response<ProfileNewsData>

    @PUT(PUBLISHERS_FOLLOW)
    suspend fun addPublisher(
        @Path("publisher_id")
        publisherId: Long
    ): Response<ProfileItem>

    @DELETE(PUBLISHERS_FOLLOW)
    suspend fun deletePublisher(
        @Path("publisher_id")
        publisherId: Long
    ): Response<ProfileItem>

    @PATCH(NEWS_UPDATE)
    suspend fun updateNews(
        @Path("global_id")
        id: String,
        @Body data: NewsUpdateRequest
    ): Response<NewsResponse>

    @DELETE(NEWS_UPDATE)
    suspend fun deleteNews(
        @Path("global_id")
        id: String
    ): Response<Unit>

    companion object {
        private const val PROFILE = "api/profile"
        private const val USER_PROFILE = "api/profile/{global_id}"
        private const val PROFILE_IMAGE = "api/profile_image"
        private const val PROFILE_NEWS = "api/my_columnnews"
        private const val NEWS_UPDATE = "api/news_update/{global_id}"
        private const val USER_PROFILE_NEWS = "feed/users/{publisher_id}"
        private const val PUBLISHERS_FOLLOW = "api/profile/publishers_subscription/{publisher_id}"
    }
}