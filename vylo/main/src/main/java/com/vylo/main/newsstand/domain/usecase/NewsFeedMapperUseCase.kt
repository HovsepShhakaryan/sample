package com.vylo.main.newsstand.domain.usecase

import com.vylo.main.homefragment.domain.entity.response.FeedData
import com.vylo.main.homefragment.domain.entity.response.FeedItem

interface NewsFeedMapperUseCase {
    fun fromNewsFeedDataToFeedItemList(feedData: FeedData): List<FeedItem>?
    fun getPagingToken(): String?
}