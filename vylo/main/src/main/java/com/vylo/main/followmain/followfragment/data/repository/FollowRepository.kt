package com.vylo.main.followmain.followfragment.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.component.entity.ProfileItem
import com.vylo.main.component.entity.PublishersSubscriptionData

interface FollowRepository {
    suspend fun getFollowers(
        id: String?,
        pageSize: Int?,
        cursor: String?
    ): Resource<PublishersSubscriptionData>

    suspend fun getFollowing(
        id: String?,
        pageSize: Int?,
        cursor: String?
    ): Resource<PublishersSubscriptionData>

    suspend fun addFollowing(publisherId: Long): Resource<ProfileItem>
    suspend fun deleteFollowing(publisherId: Long): Resource<ProfileItem>
}