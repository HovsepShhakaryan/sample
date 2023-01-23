package com.vylo.main.homefragment.domain.usecase
import com.vylo.main.homefragment.domain.entity.response.FeedData
import com.vylo.main.homefragment.domain.entity.response.FeedItem

interface FeedMapperUseCase {
    fun fromFeedDataToFeedItemList(feedData: FeedData): List<FeedItem>?
    fun getPagingToken(): String?
}