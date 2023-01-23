package com.vylo.main.categorymain.categorytrendingfragment.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.categorymain.categorytrendingfragment.data.endpoint.CategoryApi
import com.vylo.main.categorymain.categorytrendingfragment.data.repository.CategoryRepository
import com.vylo.main.followmain.followingfragment.data.endpoint.FollowingApi
import org.koin.java.KoinJavaComponent

class CategoryRepositoryImp(
    context: Context
) : BaseRepo(), CategoryRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(CategoryApi::class.java)

    private val apiProfile: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val clientProfile = apiProfile.getInstance(context).create(FollowingApi::class.java)

    override suspend fun getCategory(search: String?, cursor: String?) =
        safeApiCall { client.getCategory(search, cursor) }

    override suspend fun getProfile() = safeApiCall { clientProfile.getProfile() }

    override suspend fun addCategory(id: Long) = safeApiCall { clientProfile.addCategory(id) }

    override suspend fun deleteCategory(id: Long) = safeApiCall { clientProfile.deleteCategory(id) }
}