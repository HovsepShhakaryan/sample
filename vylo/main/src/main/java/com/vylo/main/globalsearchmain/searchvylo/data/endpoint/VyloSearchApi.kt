package com.vylo.globalsearch.searchvylo.data.endpoint

import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VyloSearchApi {

    @GET(com.vylo.main.globalsearchmain.searchvylo.data.endpoint.GLOBAL_SEARCH_VYLO_CALL)
    suspend fun searchVyloCall(
        @Query("cursor") cursorToken: String?,
        @Query("search") search: String?
    ): Response<SearchVyloData>
}