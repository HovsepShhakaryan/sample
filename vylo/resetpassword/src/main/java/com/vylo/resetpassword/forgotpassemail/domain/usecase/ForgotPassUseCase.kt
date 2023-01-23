package com.vylo.resetpassword.forgotpassemail.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.resetpassword.forgotpassemail.domain.entity.request.SendEmail
import com.vylo.resetpassword.forgotpassemail.domain.entity.response.SendEmailData

interface ForgotPassUseCase {
    suspend fun invokeEmail(sendEmail: SendEmail): Resource<SendEmailData>
    fun createEmailData(email: String): SendEmail
}