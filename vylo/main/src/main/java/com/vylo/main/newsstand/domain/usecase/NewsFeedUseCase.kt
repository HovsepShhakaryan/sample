package com.vylo.main.newsstand.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.homefragment.domain.entity.response.FeedData

interface NewsFeedUseCase {
    suspend fun invokeFeed(cursorToken: String?, id: String?): Resource<FeedData>
    suspend fun deleteNews(id: String): Resource<Unit>
}