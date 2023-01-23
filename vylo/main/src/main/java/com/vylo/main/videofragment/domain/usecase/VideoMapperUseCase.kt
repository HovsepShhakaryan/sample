package com.vylo.main.videofragment.domain.usecase

import com.vylo.main.homefragment.domain.entity.response.FeedData
import com.vylo.main.homefragment.domain.entity.response.FeedItem

interface VideoMapperUseCase {
    fun fromFeedDataToFeedItemList(feedData: FeedData): List<FeedItem>?
    fun getPagingToken(): String?
}