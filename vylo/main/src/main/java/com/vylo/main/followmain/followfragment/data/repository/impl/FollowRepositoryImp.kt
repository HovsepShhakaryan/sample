package com.vylo.main.followmain.followfragment.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.followmain.followfragment.data.endpoint.FollowApi
import com.vylo.main.followmain.followfragment.data.repository.FollowRepository
import org.koin.java.KoinJavaComponent

class FollowRepositoryImp(
    context: Context
) : BaseRepo(), FollowRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(FollowApi::class.java)

    override suspend fun getFollowers(id: String?, pageSize: Int?, cursor: String?) =
        safeApiCall { client.getFollowers(id, pageSize, cursor) }

    override suspend fun getFollowing(id: String?, pageSize: Int?, cursor: String?) =
        safeApiCall { client.getFollowing(id, pageSize, cursor) }

    override suspend fun addFollowing(publisherId: Long) =
        safeApiCall { client.addFollowing(publisherId) }

    override suspend fun deleteFollowing(publisherId: Long) =
        safeApiCall { client.deleteFollowing(publisherId) }
}