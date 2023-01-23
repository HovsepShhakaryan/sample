package com.vylo.main.categorymain.category.data.endpoint

import com.vylo.main.categorymain.category.domain.entity.Categories
import retrofit2.Response
import retrofit2.http.*

interface CategoryApi {

    @GET(CATEGORIES_CALL)
    suspend fun categories(): Response<Categories>
}