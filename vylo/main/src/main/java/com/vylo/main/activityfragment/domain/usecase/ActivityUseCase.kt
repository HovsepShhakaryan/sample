package com.vylo.main.activityfragment.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.activityfragment.domain.entity.response.ActivityData

interface ActivityUseCase {
    suspend fun getActivity(page: Int?): Resource<ActivityData>
}