package com.vylo.main.trending.data.repository

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.main.homefragment.domain.entity.response.FeedData

interface TrendingRepository {
    suspend fun getTrending(cursor: String?, categoryId: String?): Resource<FeedData>
    suspend fun getMyProfile(): Resource<ProfileItem>
    suspend fun deleteNews(id: String): Resource<Unit>
}