package com.vylo.signup.signupcomplete.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.common.domain.entity.responce.Tokens
import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupcomplete.domain.entity.SignIn
import com.vylo.signup.signupcomplete.domain.entity.UserData
import com.vylo.signup.signupglobal.entity.SignUp

interface SignUpUseCase {

    suspend fun invokeSignUp(
        signUp: SignUp
    ): Resource<SignUp>

    suspend fun invokeSignIn(
        signIn: SignIn
    ): Resource<Tokens>

    suspend fun saveRefreshToken(
        refreshToken: String
    )
    suspend fun saveToken(
        token: String
    )

    suspend fun updateUserData(userData: UserData): Resource<ProfileItem>

    suspend fun signInFacebook(
        partialToken: String,
        userData: UserData
    ): Resource<Tokens>

    suspend fun signInGoogle(
        partialToken: String,
        userData: UserData
    ): Resource<Tokens>

    suspend fun retrievePartialToken(): String?
}