package com.vylo.main.homefragment.data.repository

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.main.homefragment.domain.entity.response.FeedData

interface FeedRepository {
    suspend fun getFeedCall(cursorToken: String?): Resource<FeedData>
    suspend fun getMyProfile(): Resource<ProfileItem>
    suspend fun deleteNews(id: String): Resource<Unit>
}