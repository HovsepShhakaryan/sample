package com.vylo.main.newsstand.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.homefragment.domain.entity.response.FeedData

interface NewsFeedRepository {
    suspend fun getFeedPublishersCall(cursorToken: String?, id: String?): Resource<FeedData>
    suspend fun deleteNews(id: String): Resource<Unit>
}