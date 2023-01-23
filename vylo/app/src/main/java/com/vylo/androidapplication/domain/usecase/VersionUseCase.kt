package com.vylo.androidapplication.domain.usecase

import com.vylo.androidapplication.domain.entity.AndroidBase
import com.vylo.common.api.Resource

interface VersionUseCase {
    suspend fun getAppVersionName(): Resource<AndroidBase>
}