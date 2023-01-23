package com.vylo.androidapplication.data.endpoint

import com.vylo.androidapplication.domain.entity.AndroidBase
import retrofit2.Response
import retrofit2.http.GET

interface VersionApi {

    companion object {
        private const val APP_VERSION_NAME = "/api/env_var"
    }

    @GET(APP_VERSION_NAME)
    suspend fun getAppVersionName(): Response<AndroidBase>
}