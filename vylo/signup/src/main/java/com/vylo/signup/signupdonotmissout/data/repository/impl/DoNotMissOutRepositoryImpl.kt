package com.vylo.signup.signupdonotmissout.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.signup.signupdonotmissout.data.endpoint.DoNotMissOutApi
import com.vylo.signup.signupdonotmissout.data.repository.DoNotMissOutRepository
import org.koin.java.KoinJavaComponent

class DoNotMissOutRepositoryImpl(
    context: Context
) : BaseRepo(), DoNotMissOutRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(DoNotMissOutApi::class.java)

    override suspend fun getAutoFollow(page: Int?) = safeApiCall { client.getAutoFollow(page) }

    override suspend fun addPublisher(id: String) = safeApiCall { client.addPublisher(id) }

    override suspend fun deletePublisher(id: String) = safeApiCall { client.deletePublisher(id) }
}