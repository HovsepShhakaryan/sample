package com.vylo.signup.signupdonotmissout.data.endpoint

import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupdonotmissout.domain.entity.response.AutoFollowData
import retrofit2.Response
import retrofit2.http.*

interface DoNotMissOutApi {

    @GET(AUTO_FOLLOW)
    suspend fun getAutoFollow(
        @Query("page")
        page: Int?
    ): Response<AutoFollowData>

    @PUT(PUBLISHERS_FOLLOW)
    suspend fun addPublisher(
        @Path("publisher_id")
        publisherId: String
    ): Response<ProfileItem>

    @DELETE(PUBLISHERS_FOLLOW)
    suspend fun deletePublisher(
        @Path("publisher_id")
        publisherId: String
    ): Response<ProfileItem>

    companion object {
        private const val AUTO_FOLLOW = "api/auto_follow"
        private const val PUBLISHERS_FOLLOW = "api/profile/publishers_subscription/{publisher_id}"
    }
}