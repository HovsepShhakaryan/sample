package com.vylo.auth.signinfragment.domain.usecase.impl

import com.vylo.auth.signinfragment.data.repository.impl.SignInRepositoryImpl
import com.vylo.auth.signinfragment.domain.entity.request.SignIn
import com.vylo.auth.signinfragment.domain.usecase.SignInUseCase
import com.vylo.common.util.SharedPreferenceData

class SignInUseCaseImpl(
    private val signInRepositoryImpl: SignInRepositoryImpl,
    private val sharedPreferenceData: SharedPreferenceData
) : SignInUseCase {

    override suspend fun invokeSignIn(signIn: SignIn) = signInRepositoryImpl.postSignInCall(signIn)
    override suspend fun saveRefreshToken(refreshToken: String) { sharedPreferenceData.saveRefreshToken(refreshToken) }
    override suspend fun saveToken(token: String) { sharedPreferenceData.saveToken(token) }
}