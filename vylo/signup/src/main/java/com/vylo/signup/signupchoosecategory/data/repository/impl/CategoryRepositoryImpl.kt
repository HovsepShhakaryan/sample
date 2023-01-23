package com.vylo.signup.signupchoosecategory.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.signup.signupchoosecategory.data.endpoint.CategoryApi
import com.vylo.signup.signupchoosecategory.data.repository.CategoryRepository
import org.koin.java.KoinJavaComponent

class CategoryRepositoryImpl(
    context: Context
) : BaseRepo(), CategoryRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(CategoryApi::class.java)

    override suspend fun searchCategoryCall(cursorToken: String?, search: String?) =
        safeApiCall { client.searchCategoryCall(cursorToken, search) }

    override suspend fun addCategory(id: Long) = safeApiCall { client.addCategory(id) }

    override suspend fun deleteCategory(id: Long) = safeApiCall { client.deleteCategory(id) }
}