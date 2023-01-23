package com.vylo.androidapplication.data.repository

import com.vylo.androidapplication.domain.entity.AndroidBase
import com.vylo.common.api.Resource

interface VersionRepository {
    suspend fun getAppVersionName(): Resource<AndroidBase>
}