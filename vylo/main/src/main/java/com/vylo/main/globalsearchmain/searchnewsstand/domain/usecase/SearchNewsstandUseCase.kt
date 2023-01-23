package com.vylo.globalsearch.searchnewsstand.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandData

interface SearchNewsstandUseCase {
    suspend fun invokeSearchNewsstand(
        cursorToken: String?,
        search: String?
    ): Resource<SearchNewsstandData>
}