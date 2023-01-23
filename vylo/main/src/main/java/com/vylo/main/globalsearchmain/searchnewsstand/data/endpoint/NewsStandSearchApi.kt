package com.vylo.globalsearch.searchnewsstand.data.endpoint

import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsStandSearchApi {

    @GET(com.vylo.main.globalsearchmain.searchnewsstand.data.endpoint.GLOBAL_SEARCH_NEWSSTAND_CALL)
    suspend fun searchNewsstandCall(
        @Query("cursor") cursorToken: String?,
        @Query("search") search: String?
    ): Response<SearchNewsstandData>
}