package com.vylo.signup.signupchoosecategory.data.endpoint

import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryData
import retrofit2.Response
import retrofit2.http.*

interface CategoryApi {

    companion object {
        private const val GLOBAL_SEARCH_CATEGORIES_CALL = "api/categories"
        private const val CATEGORIES_FOLLOW = "api/profile/categories_subscription/{category_id}"
    }

    @GET(GLOBAL_SEARCH_CATEGORIES_CALL)
    suspend fun searchCategoryCall(
        @Query("cursor") cursorToken: String?,
        @Query("search") search: String?
    ): Response<CategoryData>

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