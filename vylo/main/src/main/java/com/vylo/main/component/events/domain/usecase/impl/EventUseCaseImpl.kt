package com.vylo.main.component.events.domain.usecase.impl

import com.vylo.main.component.events.data.repository.EventRepository
import com.vylo.main.component.events.domain.entity.request.ViewRequest
import com.vylo.main.component.events.domain.usecase.EventUseCase

class EventUseCaseImpl(
    private val eventRepository: EventRepository
) : EventUseCase {

    override suspend fun eventView(ids: List<String>) =
        eventRepository.eventView(ViewRequest(ids))

    override suspend fun eventSourceView(ids: List<String>) =
        eventRepository.eventSourceView(ViewRequest(ids))

    override suspend fun eventVyloView(newsGlobalId: String, timeOfView: String) =
        eventRepository.eventVyloView(newsGlobalId, timeOfView)

    override suspend fun eventShare(newsGlobalId: String) =
        eventRepository.eventShare(newsGlobalId)

    override suspend fun eventLike(newsGlobalId: String) =
        eventRepository.eventLike(newsGlobalId)

}