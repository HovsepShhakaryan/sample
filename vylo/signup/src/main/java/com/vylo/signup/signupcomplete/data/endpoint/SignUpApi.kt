package com.vylo.signup.signupcomplete.data.endpoint

import com.vylo.common.domain.entity.responce.Tokens
import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupcomplete.domain.entity.SignIn
import com.vylo.signup.signupcomplete.domain.entity.UserData
import com.vylo.signup.signupglobal.entity.SignUp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SignUpApi {

    companion object {
        const val SIGN_UP = "auth/users/"
        const val SIGN_IN = "auth/jwt/create/"
        const val UPDATE_USER_DATA = "api/profile"
        const val SIGN_UP_FACEBOOK = "auth/social/facebook/{partial_token}"
        const val SIGN_UP_GOOGLE = "auth/social/google-plus/{partial_token}"
    }

    @POST(SIGN_UP_GOOGLE)
    suspend fun signInGoogle(
        @Path("partial_token") partialToken: String,
        @Body userData: UserData
    ): Response<Tokens>

    @POST(SIGN_UP_FACEBOOK)
    suspend fun signInFacebook(
        @Path("partial_token") partialToken: String,
        @Body userData: UserData
    ): Response<Tokens>

    @POST(SIGN_UP)
    suspend fun signUp(
        @Body signUp: SignUp
    ): Response<SignUp>

    @POST(SIGN_IN)
    suspend fun signInCall(
        @Body signIn: SignIn
    ): Response<Tokens>

    @PUT(UPDATE_USER_DATA)
    suspend fun updateUserDataCall(
        @Body userData: UserData
    ): Response<ProfileItem>
}