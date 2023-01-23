package com.vylo.main.globalsearchmain.searchstartscreen.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.globalsearchmain.searchstartscreen.data.endpoint.SearchStartApi
import com.vylo.main.globalsearchmain.searchstartscreen.data.repository.SearchStartRepository
import org.koin.java.KoinJavaComponent

class SearchStartRepositoryImpl(
    context: Context
) : BaseRepo(), SearchStartRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(SearchStartApi::class.java)

    override suspend fun searchTrendingCall() =
        safeApiCall { client.searchTrendingCall() }
}