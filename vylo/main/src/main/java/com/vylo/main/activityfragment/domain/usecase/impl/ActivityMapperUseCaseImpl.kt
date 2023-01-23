package com.vylo.main.activityfragment.domain.usecase.impl

import com.vylo.main.activityfragment.domain.entity.response.ActivityData
import com.vylo.main.activityfragment.domain.entity.response.ActivityItem
import com.vylo.main.activityfragment.domain.usecase.ActivityMapperUseCase
import com.vylo.main.homefragment.data.Mapper

class ActivityMapperUseCaseImpl(
    private val mapper: Mapper
) : ActivityMapperUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun fromActivityDataToActivityItemList(data: ActivityData): List<ActivityItem>? {
        if (data.next != null)
            pagingToken = data.next
        else brakeCall = true
        return data.results
    }

    override fun getPagingToken() = mapper.getPagingQueryWithPage(pagingToken)

    fun isBrakeCall() = brakeCall
    fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }
}