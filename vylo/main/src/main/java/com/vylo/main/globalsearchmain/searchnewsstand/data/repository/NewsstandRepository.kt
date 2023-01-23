package com.vylo.globalsearch.searchnewsstand.data.repository

import com.vylo.common.api.Resource
import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandData

interface NewsstandRepository {

    suspend fun searchNewsstandCall(
        cursorToken: String?,
        search: String?
    ): Resource<SearchNewsstandData>
}