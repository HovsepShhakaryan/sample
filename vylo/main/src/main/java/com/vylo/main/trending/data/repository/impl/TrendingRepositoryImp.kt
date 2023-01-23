package com.vylo.main.trending.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.api.apiservis.ApiFeed
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.trending.data.endpoint.TrendingApi
import com.vylo.main.trending.data.repository.TrendingRepository
import org.koin.java.KoinJavaComponent

class TrendingRepositoryImp(
    context: Context
) : BaseRepo(), TrendingRepository {

    private val apiBase: ApiFeed by KoinJavaComponent.inject(ApiFeed::class.java)
    private val client = apiBase.getInstance(context).create(TrendingApi::class.java)

    private val apiProfileBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val profileClient = apiProfileBase.getInstance(context).create(TrendingApi::class.java)

    override suspend fun getTrending(cursor: String?, categoryId: String?) =
        safeApiCall { client.getTrending(cursor, categoryId) }

    override suspend fun getMyProfile() = safeApiCall { profileClient.getMyProfile() }

    override suspend fun deleteNews(id: String) = safeApiCall { profileClient.deleteNews(id) }
}