package com.vylo.main.categorymain.categorytrendingfragment.data.endpoint

import com.vylo.main.categorymain.categorytrendingfragment.domain.entity.response.SearchCategoryData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryApi {

    @GET(CATEGORY)
    suspend fun getCategory(
        @Query("search")
        search: String?,
        @Query("cursor")
        cursor: String?
    ): Response<SearchCategoryData>

    companion object {
        private const val CATEGORY = "api/categories"
    }
}