package com.vylo.main.activityfragment.domain.usecase

import com.vylo.main.activityfragment.domain.entity.response.ActivityData
import com.vylo.main.activityfragment.domain.entity.response.ActivityItem

interface ActivityMapperUseCase {
    fun fromActivityDataToActivityItemList(data: ActivityData): List<ActivityItem>?
    fun getPagingToken(): Int?
}