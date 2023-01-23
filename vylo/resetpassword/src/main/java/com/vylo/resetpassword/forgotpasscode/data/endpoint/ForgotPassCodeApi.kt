package com.vylo.resetpassword.forgotpasscode.data.endpoint

import com.vylo.resetpassword.forgotpasscode.domain.entity.ConfirmPass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ForgotPassCodeApi {

    companion object {
        const val RESET_PASSWORD = "auth/users/reset_password_confirm/"
    }

    @POST(RESET_PASSWORD)
    suspend fun sendPasswordConfirmation(
        @Body confirmPass: ConfirmPass
    ): Response<ConfirmPass>
}