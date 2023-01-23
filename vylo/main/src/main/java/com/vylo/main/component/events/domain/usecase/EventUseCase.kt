package com.vylo.main.component.events.domain.usecase

import com.vylo.common.api.Resource

interface EventUseCase {
    suspend fun eventView(ids: List<String>): Resource<Unit>
    suspend fun eventSourceView(ids: List<String>): Resource<Unit>
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