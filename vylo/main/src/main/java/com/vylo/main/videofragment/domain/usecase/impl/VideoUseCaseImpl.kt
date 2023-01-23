package com.vylo.main.videofragment.domain.usecase.impl

import com.vylo.main.videofragment.data.repository.VideoRepository
import com.vylo.main.videofragment.domain.usecase.VideoUseCase

class VideoUseCaseImpl(
    private val repository: VideoRepository
) : VideoUseCase {
    override suspend fun getResponses(id: String, cursor: String?) =
        repository.getResponses(id, cursor)

    override suspend fun getFeedById(id: String) = repository.getFeedById(id)
    override suspend fun getMyFeedById(id: String) = repository.getMyFeedById(id)
    override suspend fun deleteNews(id: String) = repository.deleteNews(id)
}