package com.vylo.main.profilefragment.domain.usecase.impl

import com.vylo.main.profilefragment.data.Mapper
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsData
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsItem
import com.vylo.main.profilefragment.domain.usecase.ProfileMapperUseCase

class ProfileMapperUseCaseImpl(
    private val mapper: Mapper
) : ProfileMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromProfileNewsDataToProfileNewsItemList(profileNewsData: ProfileNewsData): List<ProfileNewsItem>? {
        if (profileNewsData.next != null)
            pagingToken = profileNewsData.next
        else brakeCall = true
        return mapper.fromProfileNewsDataToProfileNewsItemList(profileNewsData)
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}