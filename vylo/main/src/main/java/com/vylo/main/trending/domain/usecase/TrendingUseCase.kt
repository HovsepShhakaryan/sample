package com.vylo.main.trending.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.main.homefragment.domain.entity.response.FeedData

interface TrendingUseCase {
    suspend fun getTrending(cursor: String?, categoryId: String?): Resource<FeedData>
    suspend fun saveMyGlobalId(globalId: String)
    suspend fun getMyProfile(): Resource<ProfileItem>
    suspend fun deleteNews(id: String): Resource<Unit>
}