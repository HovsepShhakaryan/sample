package com.vylo.globalsearch.searchprofiles.domain.usecase.impl

import com.vylo.common.entity.PublishersSubscription
import com.vylo.main.globalsearchmain.searchprofiles.data.repository.Mapper
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileData
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileItem
import com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.SearchProfileMapperUseCase

class SearchProfileMapperUseCaseImpl(
    private val mapper: Mapper
) : SearchProfileMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromSearchProfileDataToSearchProfileItemList(searchProfileData: ProfileData): List<ProfileItem>? {
        if (searchProfileData.next != null)
            pagingToken = searchProfileData.next
        else brakeCall = true
        return mapper.fromSearchProfileDataToSearchProfileItemList(searchProfileData)
    }

    override fun fromSearchProfileDataToPublishersSubscriptionList(searchProfileData: ProfileData): List<PublishersSubscription>? {
        if (searchProfileData.next != null) {
            pagingToken = searchProfileData.next
        } else brakeCall = true
        return mapper.fromSearchProfileDataToPublishersSubscriptionList(searchProfileData)
    }

    override fun getPagingToken(): String? {
        return mapper.getPagingQuery(pagingToken)
    }

    override fun isBrakeCall() = brakeCall
    override fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}