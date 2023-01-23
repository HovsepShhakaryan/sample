package com.vylo.main.followmain.followingfragment.data.endpoint

import com.vylo.common.entity.ProfileItem
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileData
import retrofit2.Response
import retrofit2.http.*

interface FollowingApi {

    @GET(PROFILE)
    suspend fun getProfile(): Response<ProfileItem>

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

    @PUT(CATEGORIES_FOLLOW)
    suspend fun addCategory(
        @Path("category_id")
        categoryId: Long
    ): Response<ProfileItem>

    @DELETE(CATEGORIES_FOLLOW)
    suspend fun deleteCategory(
        @Path("category_id")
        categoryId: Long
    ): Response<ProfileItem>

    @GET(SEARCH_PROFILE)
    suspend fun searchProfileUsersCall(
        @Query("page") page: String?,
        @Query("search") search: String?
    ): Response<ProfileData>

    @GET(SEARCH_NEWSSTAND)
    suspend fun searchProfilePublishersCall(
        @Query("page") page: String?,
        @Query("search") search: String?
    ): Response<ProfileData>

    companion object {
        private const val SEARCH_PROFILE = "api/vylo_publishers"
        private const val SEARCH_NEWSSTAND = "api/newsstand"
        private const val PROFILE = "api/profile"
        private const val PUBLISHERS_FOLLOW = "api/profile/publishers_subscription/{publisher_id}"
        private const val CATEGORIES_FOLLOW = "api/profile/categories_subscription/{category_id}"
    }
}