package com.vylo.resetpassword.forgotpassemail.data.repository

import com.vylo.common.api.Resource
import com.vylo.resetpassword.forgotpassemail.domain.entity.request.SendEmail
import com.vylo.resetpassword.forgotpassemail.domain.entity.response.SendEmailData

interface ForgotPassRepository {

    suspend fun sendEmail(
        sendEmail: SendEmail
    ): Resource<SendEmailData>
}