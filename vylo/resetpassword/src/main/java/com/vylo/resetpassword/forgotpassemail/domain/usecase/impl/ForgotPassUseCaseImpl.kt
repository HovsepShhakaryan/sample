package com.vylo.resetpassword.forgotpassemail.domain.usecase.impl

import com.vylo.resetpassword.forgotpassemail.data.repository.ForgotPassRepository
import com.vylo.resetpassword.forgotpassemail.domain.entity.request.SendEmail
import com.vylo.resetpassword.forgotpassemail.domain.usecase.ForgotPassUseCase

class ForgotPassUseCaseImpl(
    private val forgotPassRepository: ForgotPassRepository
) : ForgotPassUseCase {

    override suspend fun invokeEmail(sendEmail: SendEmail) =
        forgotPassRepository.sendEmail(sendEmail)

    override fun createEmailData(email: String) =
        SendEmail(
            email
        )
}