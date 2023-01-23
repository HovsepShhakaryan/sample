package com.vylo.main.globalsearchmain.searchcategories.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.globalsearchmain.searchcategories.data.endpoint.CategorySearchApi
import com.vylo.globalsearch.searchcategories.data.repository.SearchCategoryRepository
import org.koin.java.KoinJavaComponent

class SearchCategoryRepositoryImpl(
    context: Context
) : BaseRepo(), SearchCategoryRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(CategorySearchApi::class.java)

    override suspend fun searchCategoryCall(cursorToken: String?, search: String?) =
        safeApiCall { client.searchCategoryCall(cursorToken, search) }

    override suspend fun addCategory(id: Long) = safeApiCall { client.addCategory(id) }

    override suspend fun deleteCategory(id: Long) = safeApiCall { client.deleteCategory(id) }

    override suspend fun getProfile() = safeApiCall { client.getProfile() }

}