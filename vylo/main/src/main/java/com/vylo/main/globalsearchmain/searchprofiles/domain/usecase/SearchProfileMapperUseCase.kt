package com.vylo.main.globalsearchmain.searchprofiles.domain.usecase

import com.vylo.common.entity.PublishersSubscription
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileData
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileItem

interface SearchProfileMapperUseCase {
    fun fromSearchProfileDataToSearchProfileItemList(searchProfileData: ProfileData): List<ProfileItem>?
    fun fromSearchProfileDataToPublishersSubscriptionList(searchProfileData: ProfileData): List<PublishersSubscription>?
    fun getPagingToken(): String?
    fun setBrakeCall(isBrakeCall: Boolean)
    fun isBrakeCall(): Boolean
}