package com.vylo.main.newsstand.domain.usecase.impl

import com.vylo.main.newsstand.data.repository.impl.NewsFeedRepositoryImpl
import com.vylo.main.newsstand.domain.usecase.NewsFeedUseCase

class NewsFeedUseCaseImpl(
    private val newsFeedRepositoryImpl: NewsFeedRepositoryImpl
) : NewsFeedUseCase {

    override suspend fun invokeFeed(cursorToken: String?, id: String?) =
        newsFeedRepositoryImpl.getFeedPublishersCall(cursorToken, id)

    override suspend fun deleteNews(id: String) = newsFeedRepositoryImpl.deleteNews(id)
}