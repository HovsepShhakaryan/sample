package com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.globalsearchmain.searchstartscreen.domain.entiry.response.TrendingData

interface SearchStartUseCase {
    suspend fun invokeTrending(): Resource<List<TrendingData>>
}