package com.vylo.globalsearch.searchvylo.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloData

interface SearchVyloUseCase {
    suspend fun invokeSearchVylo(
        cursorToken: String?,
        search: String?
    ): Resource<SearchVyloData>
}