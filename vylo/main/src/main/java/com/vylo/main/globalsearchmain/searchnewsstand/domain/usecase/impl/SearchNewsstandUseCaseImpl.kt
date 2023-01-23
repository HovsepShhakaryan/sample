package com.vylo.globalsearch.searchnewsstand.domain.usecase.impl

import com.vylo.globalsearch.searchnewsstand.data.repository.impl.NewsstandRepositoryImpl
import com.vylo.globalsearch.searchnewsstand.domain.usecase.SearchNewsstandUseCase

class SearchNewsstandUseCaseImpl(
    private val searchNewsstandRepositoryImpl: NewsstandRepositoryImpl
) : SearchNewsstandUseCase {

    override suspend fun invokeSearchNewsstand(cursorToken: String?, search: String?) =
        searchNewsstandRepositoryImpl.searchNewsstandCall(cursorToken, search)
}