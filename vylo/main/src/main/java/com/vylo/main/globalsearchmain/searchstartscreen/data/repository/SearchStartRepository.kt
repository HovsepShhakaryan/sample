package com.vylo.main.globalsearchmain.searchstartscreen.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.globalsearchmain.searchstartscreen.domain.entiry.response.TrendingData

interface SearchStartRepository {
    suspend fun searchTrendingCall(): Resource<List<TrendingData>>
}