package com.vylo.main.globalsearchmain.searchprofiles.data.endpoint

import com.vylo.common.entity.ProfileItem
import com.vylo.main.followmain.followingfragment.data.endpoint.FollowingApi
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileData
import retrofit2.Response
import retrofit2.http.*

interface ProfileSearchApi {

    companion object {
        private const val PROFILE = "api/profile"
        private const val SEARCH_PROFILE = "api/publishers"
        private const val SUBSCRIBE_PUBLISHER = "api/profile/publishers_subscription/{id}"
    }

    @GET(PROFILE)
    suspend fun getProfile(): Response<ProfileItem>

    @GET(SEARCH_PROFILE)
    suspend fun searchProfileCall(
        @Query("page") page: String?,
        @Query("search") search: String?
    ): Response<ProfileData>

    @PUT(SUBSCRIBE_PUBLISHER)
    suspend fun subscribePublisher(
        @Path("id")
        publisherId: Long
    ): Response<ProfileItem>

    @DELETE(SUBSCRIBE_PUBLISHER)
    suspend fun deleteSubscribedPublisher(
        @Path("id")
        categoryId: Long
    ): Response<ProfileItem>
}