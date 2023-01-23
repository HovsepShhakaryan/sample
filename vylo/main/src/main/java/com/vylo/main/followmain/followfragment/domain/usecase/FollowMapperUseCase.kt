package com.vylo.main.followmain.followfragment.domain.usecase

import com.vylo.main.component.entity.PublishersSubscription
import com.vylo.main.component.entity.PublishersSubscriptionData

interface FollowMapperUseCase {
    fun fromPublishersSubscriptionDataToPublishersSubscriptionList(publishersSubscriptionData: PublishersSubscriptionData): List<PublishersSubscription>?
    fun getPagingToken(): String?
}