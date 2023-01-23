package com.vylo.signup.signupdonotmissout.domain.usecase.impl

import com.vylo.signup.signupdonotmissout.data.Mapper
import com.vylo.signup.signupdonotmissout.domain.entity.response.AutoFollowData
import com.vylo.signup.signupdonotmissout.domain.entity.response.AutoFollowItem
import com.vylo.signup.signupdonotmissout.domain.usecase.DoNotMissOutMapperUseCase


class DoNotMissOutMapperUseCaseImpl(
    private val mapper: Mapper
) : DoNotMissOutMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromAutoFollowDataToAutoFollowList(data: AutoFollowData): List<AutoFollowItem>? {
        if (data.next != null)
            pagingToken = data.next
        else brakeCall = true
        return mapper.fromAutoFollowDataToAutoFollowItemList(data)
    }

    override fun getPagingToken() = mapper.getPagingQuery(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}