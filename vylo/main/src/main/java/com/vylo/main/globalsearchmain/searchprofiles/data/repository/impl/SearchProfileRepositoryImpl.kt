package com.vylo.main.globalsearchmain.searchprofiles.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.globalsearchmain.searchprofiles.data.endpoint.ProfileSearchApi
import com.vylo.main.globalsearchmain.searchprofiles.data.repository.SearchProfileRepository
import org.koin.java.KoinJavaComponent

class SearchProfileRepositoryImpl(
    context: Context
) : BaseRepo(), SearchProfileRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(ProfileSearchApi::class.java)

    override suspend fun getProfile() = safeApiCall { client.getProfile() }

    override suspend fun searchProfileCall(page: String?, search: String?) =
        safeApiCall { client.searchProfileCall(page, search) }

    override suspend fun subscribePublisher(id: Long) = safeApiCall { client.subscribePublisher(id) }

    override suspend fun deleteSubscribedPublisher(id: Long) = safeApiCall { client.deleteSubscribedPublisher(id) }
}