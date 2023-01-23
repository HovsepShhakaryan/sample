package com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.impl

import com.vylo.main.globalsearchmain.searchstartscreen.data.Mapper
import com.vylo.main.globalsearchmain.searchstartscreen.domain.entiry.response.TrendingData
import com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.SearchStartMapperUseCase

class SearchStartMapperUseCaseImpl(
    private val mapper: Mapper
) : SearchStartMapperUseCase {

    override fun fromTrendingListToStringList(trandingData: List<TrendingData>) =
        mapper.getTrending(trandingData)
}