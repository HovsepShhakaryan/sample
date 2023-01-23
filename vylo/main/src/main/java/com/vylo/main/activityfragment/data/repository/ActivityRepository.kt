package com.vylo.main.activityfragment.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.activityfragment.domain.entity.response.ActivityData

interface ActivityRepository {
    suspend fun getActivity(page: Int?): Resource<ActivityData>
}