package com.vylo.main.homefragment.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.api.apiservis.ApiFeed
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.homefragment.data.endpoint.FeedApi
import com.vylo.main.homefragment.data.repository.FeedRepository
import org.koin.java.KoinJavaComponent

class FeedRepositoryImpl(
    context: Context
) : BaseRepo(), FeedRepository {

    private val apiBase: ApiFeed by KoinJavaComponent.inject(ApiFeed::class.java)
    private val client = apiBase.getInstance(context).create(FeedApi::class.java)

    private val apiProfileBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val profileClient = apiProfileBase.getInstance(context).create(FeedApi::class.java)

    override suspend fun getFeedCall(cursorToken: String?) =
        safeApiCall { client.getFeedsCall(cursorToken) }

    override suspend fun getMyProfile() = safeApiCall { profileClient.getMyProfile() }

    override suspend fun deleteNews(id: String) = safeApiCall { profileClient.deleteNews(id) }
}