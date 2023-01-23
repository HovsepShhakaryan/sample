package com.vylo.main.videofragment.domain.usecase.impl

import com.vylo.main.homefragment.domain.entity.response.FeedData
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.videofragment.domain.usecase.VideoMapperUseCase

class VideoMapperUseCaseImpl(
    private val mapper: com.vylo.main.videofragment.data.Mapper
) : VideoMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromFeedDataToFeedItemList(feedData: FeedData): List<FeedItem>? {
        if (feedData.next != null)
            pagingToken = feedData.next
        else brakeCall = true
        return mapper.fromFeedDataToFeedItemList(feedData)
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}