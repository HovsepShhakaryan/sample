package com.vylo.globalsearch.searchvylo.data.repository

import com.vylo.common.api.Resource
import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloData

interface SearchVyloRepository {

    suspend fun searchVyloCall(
        cursorToken: String?,
        search: String?
    ): Resource<SearchVyloData>
}