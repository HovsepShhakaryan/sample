package com.vylo.signup.signupcreateusername.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.signup.signupcreateusername.data.endpoint.SignUpUsernameApi
import com.vylo.signup.signupcreateusername.data.repository.SignUpUsernameRepository
import com.vylo.signup.signupcreateusername.domain.entity.request.DraftUserName
import org.koin.java.KoinJavaComponent

class SignUpUsernameRepositoryImpl(
    context: Context
) : BaseRepo(), SignUpUsernameRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(SignUpUsernameApi::class.java)

    override suspend fun checkUsername(draftUserName: DraftUserName) =
        safeApiCall { client.checkUsername(draftUserName) }
}