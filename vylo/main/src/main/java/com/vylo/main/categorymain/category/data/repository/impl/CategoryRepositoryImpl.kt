package com.vylo.main.categorymain.category.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.categorymain.category.data.endpoint.CategoryApi
import com.vylo.main.categorymain.category.data.repository.CategoryRepository
import org.koin.java.KoinJavaComponent

class CategoryRepositoryImpl(
    context: Context
) : BaseRepo() ,CategoryRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(CategoryApi::class.java)

    override suspend fun invokeCategories() = safeApiCall { client.categories() }
}