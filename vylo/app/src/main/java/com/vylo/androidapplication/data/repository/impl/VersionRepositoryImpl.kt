package com.vylo.androidapplication.data.repository.impl

import android.content.Context
import com.vylo.androidapplication.data.endpoint.VersionApi
import com.vylo.androidapplication.data.repository.VersionRepository
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.api.apiservis.ApiFeed
import com.vylo.common.data.repository.BaseRepo
import org.koin.java.KoinJavaComponent

class VersionRepositoryImpl(
    context: Context
) : BaseRepo(), VersionRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(VersionApi::class.java)

    override suspend fun getAppVersionName() =
        safeApiCall { client.getAppVersionName() }
}