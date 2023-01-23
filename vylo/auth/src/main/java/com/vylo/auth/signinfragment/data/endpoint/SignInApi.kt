package com.vylo.auth.signinfragment.data.endpoint

import com.vylo.auth.signinfragment.domain.entity.request.SignIn
import com.vylo.common.domain.entity.responce.Tokens
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInApi {

    @POST(SIGN_IN_CALL)
    suspend fun signInCall(@Body signIn: SignIn): Response<Tokens>
}