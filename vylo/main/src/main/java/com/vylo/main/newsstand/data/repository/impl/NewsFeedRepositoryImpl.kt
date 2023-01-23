package com.vylo.main.newsstand.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.api.apiservis.ApiFeed
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.homefragment.data.endpoint.FeedApi
import com.vylo.main.newsstand.data.endpoint.NewsFeedApi
import com.vylo.main.newsstand.data.repository.NewsFeedRepository
import org.koin.java.KoinJavaComponent

class NewsFeedRepositoryImpl(
    context: Context
) : BaseRepo(), NewsFeedRepository {

    private val apiBase: ApiFeed by KoinJavaComponent.inject(ApiFeed::class.java)
    private val client = apiBase.getInstance(context).create(NewsFeedApi::class.java)

    private val apiProfileBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val profileClient = apiProfileBase.getInstance(context).create(FeedApi::class.java)

    override suspend fun getFeedPublishersCall(cursorToken: String?, id: String?) =
        safeApiCall { client.getFeedPublishersCall(cursorToken, id) }

    override suspend fun deleteNews(id: String) = safeApiCall { profileClient.deleteNews(id) }
}