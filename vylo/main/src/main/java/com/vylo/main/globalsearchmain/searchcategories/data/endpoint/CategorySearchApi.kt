package com.vylo.main.globalsearchmain.searchcategories.data.endpoint

import com.vylo.common.entity.ProfileItem
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.SearchCategoryData
import retrofit2.Response
import retrofit2.http.*

interface CategorySearchApi {

    companion object {
        private const val PROFILE = "api/profile"
        private const val GLOBAL_SEARCH_CATEGORIES_CALL = "api/categories"
        private const val CATEGORIES_FOLLOW = "api/profile/categories_subscription/{category_id}"
    }

    @GET(PROFILE)
    suspend fun getProfile(): Response<ProfileItem>

    @GET(GLOBAL_SEARCH_CATEGORIES_CALL)
    suspend fun searchCategoryCall(
        @Query("cursor") cursorToken: String?,
        @Query("search") search: String?
    ): Response<SearchCategoryData>

    @PUT(CATEGORIES_FOLLOW)
    suspend fun addCategory(
        @Path("category_id")
        categoryId: Long
    ): Response<ProfileItem>

    @DELETE(CATEGORIES_FOLLOW)
    suspend fun deleteCategory(
        @Path("category_id")
        categoryId: Long
    ): Response<ProfileItem>

}