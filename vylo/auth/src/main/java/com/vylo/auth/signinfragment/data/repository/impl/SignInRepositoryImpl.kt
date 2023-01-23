package com.vylo.auth.signinfragment.data.repository.impl

import android.content.Context
import com.vylo.auth.signinfragment.data.endpoint.SignInApi
import com.vylo.auth.signinfragment.data.repository.SignInRepository
import com.vylo.auth.signinfragment.domain.entity.request.SignIn
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import org.koin.java.KoinJavaComponent

class SignInRepositoryImpl(
    context: Context
) : BaseRepo(), SignInRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(SignInApi::class.java)

    override suspend fun postSignInCall(signIn: SignIn) = safeApiCall { client.signInCall(signIn) }


}