package com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase

import com.vylo.main.globalsearchmain.searchstartscreen.domain.entiry.response.TrendingData

interface SearchStartMapperUseCase {
    fun fromTrendingListToStringList(trandingData: List<TrendingData>): List<String>
}