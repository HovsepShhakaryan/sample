package com.vylo.resetpassword.forgotpassemail.data.endpoint

import com.vylo.resetpassword.forgotpassemail.domain.entity.request.SendEmail
import com.vylo.resetpassword.forgotpassemail.domain.entity.response.SendEmailData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ForgotPassApi {

    companion object {
        const val RESET_PASSWORD = "auth/users/reset_password/"
    }

    @POST(RESET_PASSWORD)
    suspend fun sendEmail(
        @Body sendEmail: SendEmail
    ): Response<SendEmailData>
}