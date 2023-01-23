package com.vylo.globalsearch.searchvylo.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiFeed
import com.vylo.common.data.repository.BaseRepo
import com.vylo.globalsearch.searchvylo.data.endpoint.VyloSearchApi
import com.vylo.globalsearch.searchvylo.data.repository.SearchVyloRepository
import org.koin.java.KoinJavaComponent

class SearchVyloRepositoryImpl(
    context: Context
) : BaseRepo(), SearchVyloRepository {

    private val apiBase: ApiFeed by KoinJavaComponent.inject(ApiFeed::class.java)
    private val client = apiBase.getInstance(context).create(VyloSearchApi::class.java)

    override suspend fun searchVyloCall(cursorToken: String?, search: String?) =
        safeApiCall { client.searchVyloCall(cursorToken, search) }

}