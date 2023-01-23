package com.vylo.main.activityfragment.domain.usecase.impl

import com.vylo.main.activityfragment.data.repository.ActivityRepository
import com.vylo.main.activityfragment.domain.usecase.ActivityUseCase

class ActivityUseCaseImpl(
    private val repository: ActivityRepository
) : ActivityUseCase {
    override suspend fun getActivity(page: Int?) =
        repository.getActivity(page)
}