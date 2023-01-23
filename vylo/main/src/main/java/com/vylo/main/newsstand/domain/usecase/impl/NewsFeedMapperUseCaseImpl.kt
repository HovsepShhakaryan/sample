package com.vylo.main.newsstand.domain.usecase.impl

import com.vylo.main.homefragment.domain.entity.response.FeedData
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.newsstand.data.Mapper
import com.vylo.main.newsstand.domain.usecase.NewsFeedMapperUseCase

class NewsFeedMapperUseCaseImpl(
    private val mapper: Mapper
) : NewsFeedMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromNewsFeedDataToFeedItemList(feedData: FeedData): List<FeedItem>? {
        if (feedData.next != null)
            pagingToken = feedData.next
        else brakeCall = true
        return mapper.fromNewsFeedDataToNewsFeedItemList(feedData)
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}