package com.vylo.auth.signinfragment.domain.usecase

import com.vylo.auth.signinfragment.domain.entity.request.SignIn
import com.vylo.common.domain.entity.responce.Tokens
import com.vylo.common.api.Resource

interface SignInUseCase {
    suspend fun invokeSignIn(signIn: SignIn): Resource<Tokens>
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun saveToken(token: String)
}