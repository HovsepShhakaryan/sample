package com.vylo.main.videofragment.data.repository.impl

import android.content.Context
import com.vylo.common.api.Resource
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.api.apiservis.ApiFeed
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.homefragment.data.endpoint.FeedApi
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.videofragment.data.endpoint.VideoApi
import com.vylo.main.videofragment.data.repository.VideoRepository
import org.koin.java.KoinJavaComponent

class VideoRepositoryImpl(
    context: Context
) : BaseRepo(), VideoRepository {

    private val apiBase: ApiFeed by KoinJavaComponent.inject(ApiFeed::class.java)
    private val client = apiBase.getInstance(context).create(VideoApi::class.java)
    private val apiColumn: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val clientColumn = apiColumn.getInstance(context).create(VideoApi::class.java)

    private val apiProfileBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val profileClient = apiProfileBase.getInstance(context).create(FeedApi::class.java)

    override suspend fun getResponses(id: String, cursor: String?) =
        safeApiCall { client.getResponses(id, cursor) }

    override suspend fun getFeedById(id: String) = safeApiCall { client.getFeedById(id) }
    override suspend fun getMyFeedById(id: String) = safeApiCall { clientColumn.getMyFeedById(id) }

    override suspend fun deleteNews(id: String) = safeApiCall { profileClient.deleteNews(id) }
}