package com.vylo.main.insightfulfragment.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiEvents
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.insightfulfragment.data.endpoint.InsightfulApi
import com.vylo.main.insightfulfragment.data.repository.InsightfulRepository
import org.koin.java.KoinJavaComponent

class InsightfulRepositoryImp(
    context: Context
) : BaseRepo(), InsightfulRepository {

    private val apiBase: ApiEvents by KoinJavaComponent.inject(ApiEvents::class.java)
    private val client = apiBase.getInstance(context).create(InsightfulApi::class.java)

    override suspend fun getInsightful(cursor: String?) =
        safeApiCall { client.getInsightful(cursor) }
}