package com.vylo.globalsearch.searchvylo.domain.usecase.impl

import com.vylo.globalsearch.searchvylo.data.repository.impl.SearchVyloRepositoryImpl
import com.vylo.globalsearch.searchvylo.domain.usecase.SearchVyloUseCase

class SearchVyloUseCaseImpl(
    private val searchVyloRepositoryImpl: SearchVyloRepositoryImpl
) : SearchVyloUseCase {

    override suspend fun invokeSearchVylo(cursorToken: String?, search: String?) =
        searchVyloRepositoryImpl.searchVyloCall(cursorToken, search)
}