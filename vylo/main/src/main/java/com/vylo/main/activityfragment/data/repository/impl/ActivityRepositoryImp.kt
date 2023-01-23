package com.vylo.main.activityfragment.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiEvents
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.activityfragment.data.endpoint.ActivityApi
import com.vylo.main.activityfragment.data.repository.ActivityRepository
import org.koin.java.KoinJavaComponent

class ActivityRepositoryImp(
    context: Context
) : BaseRepo(), ActivityRepository {

    private val apiBase: ApiEvents by KoinJavaComponent.inject(ApiEvents::class.java)
    private val client = apiBase.getInstance(context).create(ActivityApi::class.java)

    override suspend fun getActivity(page: Int?) =
        safeApiCall { client.getActivity(page) }
}