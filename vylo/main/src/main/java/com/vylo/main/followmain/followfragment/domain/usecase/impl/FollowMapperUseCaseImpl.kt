package com.vylo.main.followmain.followfragment.domain.usecase.impl

import com.vylo.main.component.entity.PublishersSubscription
import com.vylo.main.component.entity.PublishersSubscriptionData
import com.vylo.main.followmain.followfragment.data.Mapper
import com.vylo.main.followmain.followfragment.domain.usecase.FollowMapperUseCase

class FollowMapperUseCaseImpl(
    private val mapper: Mapper
) : FollowMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromPublishersSubscriptionDataToPublishersSubscriptionList(publishersSubscriptionData: PublishersSubscriptionData): List<PublishersSubscription>? {
        if (publishersSubscriptionData.next != null)
            pagingToken = publishersSubscriptionData.next
        else brakeCall = true
        return mapper.fromPublishersSubscriptionDataToPublishersSubscriptionList(publishersSubscriptionData)
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}