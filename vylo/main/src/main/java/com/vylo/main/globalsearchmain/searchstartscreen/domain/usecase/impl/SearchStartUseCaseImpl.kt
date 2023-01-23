package com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.impl

import com.vylo.main.globalsearchmain.searchstartscreen.data.repository.SearchStartRepository
import com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.SearchStartUseCase

class SearchStartUseCaseImpl(
    private val startSearchStartRepository: SearchStartRepository
) : SearchStartUseCase {

    override suspend fun invokeTrending() = startSearchStartRepository.searchTrendingCall()
}