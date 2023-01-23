package com.vylo.main.videofragment.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.homefragment.domain.entity.response.FeedData
import com.vylo.main.homefragment.domain.entity.response.FeedItem

interface VideoUseCase {
    suspend fun getResponses(id: String, cursor: String?): Resource<FeedData>
    suspend fun getFeedById(id: String): Resource<FeedItem>
    suspend fun getMyFeedById(id: String): Resource<FeedItem>
    suspend fun deleteNews(id: String): Resource<Unit>
}