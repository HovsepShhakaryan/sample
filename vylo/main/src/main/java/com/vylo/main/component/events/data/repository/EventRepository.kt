package com.vylo.main.component.events.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.component.events.domain.entity.request.ViewRequest
import retrofit2.Response
import retrofit2.http.Path

interface EventRepository {
    suspend fun eventView(viewRequest: ViewRequest): Resource<Unit>
    suspend fun eventSourceView(viewRequest: ViewRequest): Resource<Unit>
    suspend fun eventVyloView(
        newsGlobalId: String,
        timeOfView: String
    ): Resource<Unit>

    suspend fun eventShare(
        newsGlobalId: String
    ): Resource<Unit>

    suspend fun eventLike(
        newsGlobalId: String
    ): Resource<Unit>
}