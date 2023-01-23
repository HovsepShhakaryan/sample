package com.vylo.main.component.events.data.repository.impl

import android.content.Context
import com.vylo.common.api.Resource
import com.vylo.common.api.apiservis.ApiEvents
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.component.events.data.enpoint.EventApi
import com.vylo.main.component.events.domain.entity.request.ViewRequest
import com.vylo.main.component.events.data.repository.EventRepository
import org.koin.java.KoinJavaComponent
import retrofit2.Response

class EventRepositoryImpl(
    context: Context
) : BaseRepo(), EventRepository {

    private val apiEvents: ApiEvents by KoinJavaComponent.inject(ApiEvents::class.java)
    private val clientEvents = apiEvents.getInstance(context).create(EventApi::class.java)

    override suspend fun eventView(viewRequest: ViewRequest) =
        safeApiCall { clientEvents.eventView(viewRequest) }

    override suspend fun eventSourceView(viewRequest: ViewRequest) =
        safeApiCall { clientEvents.eventSourceView(viewRequest) }

    override suspend fun eventVyloView(newsGlobalId: String, timeOfView: String) =
        safeApiCall { clientEvents.eventVyloView(newsGlobalId, timeOfView) }

    override suspend fun eventShare(newsGlobalId: String) =
        safeApiCall { clientEvents.eventShare(newsGlobalId) }

    override suspend fun eventLike(newsGlobalId: String) =
        safeApiCall { clientEvents.eventLike(newsGlobalId) }

}