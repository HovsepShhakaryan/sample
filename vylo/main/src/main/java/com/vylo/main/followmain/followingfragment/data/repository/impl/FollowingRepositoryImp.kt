package com.vylo.main.followmain.followingfragment.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.followmain.followingfragment.data.endpoint.FollowingApi
import com.vylo.main.followmain.followingfragment.data.repository.FollowingRepository
import org.koin.java.KoinJavaComponent

class FollowingRepositoryImp(
    context: Context
) : BaseRepo(), FollowingRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(FollowingApi::class.java)

    override suspend fun getProfile() = safeApiCall { client.getProfile() }

    override suspend fun addPublisher(id: Long) = safeApiCall { client.addPublisher(id) }

    override suspend fun deletePublisher(id: Long) = safeApiCall { client.deletePublisher(id) }

    override suspend fun addCategory(id: Long) = safeApiCall { client.addCategory(id) }

    override suspend fun deleteCategory(id: Long) = safeApiCall { client.deleteCategory(id) }

    override suspend fun searchProfileUsersCall(page: String?, search: String?) =
        safeApiCall { client.searchProfileUsersCall(page, search) }

    override suspend fun searchProfilePublishersCall(page: String?, search: String?) =
        safeApiCall { client.searchProfilePublishersCall(page, search) }
}