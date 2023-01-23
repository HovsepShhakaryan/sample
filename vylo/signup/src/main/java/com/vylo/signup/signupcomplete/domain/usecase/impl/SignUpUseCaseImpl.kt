package com.vylo.signup.signupcomplete.domain.usecase.impl

import com.vylo.common.util.SharedPreferenceData
import com.vylo.signup.signupcomplete.data.repository.SignUpRepository
import com.vylo.signup.signupcomplete.domain.entity.SignIn
import com.vylo.signup.signupcomplete.domain.entity.UserData
import com.vylo.signup.signupcomplete.domain.usecase.SignUpUseCase
import com.vylo.signup.signupglobal.entity.SignUp

class SignUpUseCaseImpl(
    private val signUpRepository: SignUpRepository,
    private val sharedPreferenceData: SharedPreferenceData
) : SignUpUseCase {

    override suspend fun invokeSignUp(signUp: SignUp) =
        signUpRepository.invokeSignUp(signUp)

    override suspend fun invokeSignIn(signIn: SignIn) =
        signUpRepository.postSignInCall(signIn)

    override suspend fun updateUserData(userData: UserData) =
        signUpRepository.updateUserData(userData)

    override suspend fun signInFacebook(partialToken: String, userData: UserData) =
        signUpRepository.signInFacebook(partialToken, userData)

    override suspend fun signInGoogle(partialToken: String, userData: UserData) =
        signUpRepository.signInGoogle(partialToken, userData)

    override suspend fun retrievePartialToken() = sharedPreferenceData.retrievePartialToken

    override suspend fun saveRefreshToken(refreshToken: String) { sharedPreferenceData.saveRefreshToken(refreshToken) }
    override suspend fun saveToken(token: String) { sharedPreferenceData.saveToken(token) }
}