package com.vylo.main.globalsearchmain.searchstartscreen.data.endpoint

import com.vylo.main.globalsearchmain.searchstartscreen.domain.entiry.response.TrendingData
import retrofit2.Response
import retrofit2.http.GET

interface SearchStartApi {

    companion object {
        private const val SEARCH_TRENDING = "api/search_trending"
    }

    @GET(SEARCH_TRENDING)
    suspend fun searchTrendingCall(): Response<List<TrendingData>>
}