package com.vylo.signup.signupcomplete.data.repository.impl

import android.content.Context
import com.vylo.common.api.Resource
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.common.domain.entity.responce.Tokens
import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupcomplete.data.endpoint.SignUpApi
import com.vylo.signup.signupcomplete.data.repository.SignUpRepository
import com.vylo.signup.signupcomplete.domain.entity.SignIn
import com.vylo.signup.signupcomplete.domain.entity.UserData
import com.vylo.signup.signupglobal.entity.SignUp
import org.koin.java.KoinJavaComponent
import retrofit2.Response

class SignUpRepositoryImpl(
    context: Context
) : BaseRepo(), SignUpRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(SignUpApi::class.java)

    override suspend fun invokeSignUp(signUp: SignUp) =
        safeApiCall { client.signUp(signUp) }

    override suspend fun postSignInCall(signIn: SignIn) =
        safeApiCall { client.signInCall(signIn) }

    override suspend fun updateUserData(userData: UserData) =
        safeApiCall { client.updateUserDataCall(userData) }

    override suspend fun signInFacebook(partialToken: String, userData: UserData) =
        safeApiCall { client.signInFacebook(partialToken, userData) }

    override suspend fun signInGoogle(partialToken: String, userData: UserData) =
        safeApiCall { client.signInGoogle(partialToken, userData) }
}