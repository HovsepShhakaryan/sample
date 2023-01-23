package com.vylo.signup.signupcomplete.data.repository

import com.vylo.common.api.Resource
import com.vylo.common.domain.entity.responce.Tokens
import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupcomplete.domain.entity.SignIn
import com.vylo.signup.signupcomplete.domain.entity.UserData
import com.vylo.signup.signupglobal.entity.SignUp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

interface SignUpRepository {
    suspend fun invokeSignUp(
        signUp: SignUp
    ): Resource<SignUp>

    suspend fun postSignInCall(signIn: SignIn): Resource<Tokens>

    suspend fun updateUserData(userData: UserData): Resource<ProfileItem>

    suspend fun signInFacebook(
        partialToken: String,
        userData: UserData
    ): Resource<Tokens>

    suspend fun signInGoogle(
        partialToken: String,
        userData: UserData
    ): Resource<Tokens>
}