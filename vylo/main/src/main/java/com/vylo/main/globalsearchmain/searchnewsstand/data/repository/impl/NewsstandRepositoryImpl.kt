package com.vylo.globalsearch.searchnewsstand.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiFeed
import com.vylo.common.data.repository.BaseRepo
import com.vylo.globalsearch.searchnewsstand.data.endpoint.NewsStandSearchApi
import com.vylo.globalsearch.searchnewsstand.data.repository.NewsstandRepository
import org.koin.java.KoinJavaComponent

class NewsstandRepositoryImpl(
    context: Context
) : BaseRepo(), NewsstandRepository {

    private val apiBase: ApiFeed by KoinJavaComponent.inject(ApiFeed::class.java)
    private val client = apiBase.getInstance(context).create(NewsStandSearchApi::class.java)

    override suspend fun searchNewsstandCall(cursorToken: String?, search: String?) =
        safeApiCall { client.searchNewsstandCall(cursorToken, search) }
}