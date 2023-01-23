package com.vylo.main.followmain.followfragment.data.endpoint

import com.vylo.main.component.entity.ProfileItem
import com.vylo.main.component.entity.PublishersSubscriptionData
import retrofit2.Response
import retrofit2.http.*

interface FollowApi {

    @GET(FOLLOWERS)
    suspend fun getFollowers(
        @Query("profile_global_id")
        id: String?,
        @Query("page_size")
        pageSize: Int?,
        @Query("cursor")
        cursor: String?
    ): Response<PublishersSubscriptionData>

    @GET(FOLLOWING)
    suspend fun getFollowing(
        @Query("profile_global_id")
        id: String?,
        @Query("page_size")
        pageSize: Int?,
        @Query("cursor")
        cursor: String?
    ): Response<PublishersSubscriptionData>

    @PUT(ADD_FOLLOW)
    suspend fun addFollowing(
        @Path("publisher_id")
        publisherId: Long
    ): Response<ProfileItem>

    @DELETE(DELETE_FOLLOW)
    suspend fun deleteFollowing(
        @Path("publisher_id")
        publisherId: Long
    ): Response<ProfileItem>

    companion object {
        private const val FOLLOWERS = "api/profile/followers"
        private const val FOLLOWING = "api/profile/following"
        private const val ADD_FOLLOW = "api/profile/publishers_subscription/{publisher_id}"
        private const val DELETE_FOLLOW = "api/profile/publishers_subscription/{publisher_id}"
    }
}