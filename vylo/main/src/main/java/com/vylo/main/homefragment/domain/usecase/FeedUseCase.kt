package com.vylo.main.homefragment.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.main.homefragment.domain.entity.response.FeedData

interface FeedUseCase {
    suspend fun invokeFeed(cursorToken: String?): Resource<FeedData>
    suspend fun saveMyGlobalId(globalId: String)
    suspend fun getMyProfile(): Resource<ProfileItem>
    suspend fun deleteNews(id: String): Resource<Unit>
}