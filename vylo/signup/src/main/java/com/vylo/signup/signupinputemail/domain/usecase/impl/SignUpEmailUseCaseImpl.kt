package com.vylo.signup.signupinputemail.domain.usecase.impl

import com.vylo.signup.signupinputemail.data.repository.SignUpEmailRepository
import com.vylo.signup.signupinputemail.domain.entity.request.DraftEmail
import com.vylo.signup.signupinputemail.domain.usecase.SignUpEmailUseCase

class SignUpEmailUseCaseImpl(
    private val signUpEmailRepository: SignUpEmailRepository
) : SignUpEmailUseCase {

    override suspend fun invokeEmail(draftEmail: DraftEmail) =
        signUpEmailRepository.checkEmail(draftEmail)

    override fun createEmailData(email: String) =
        DraftEmail(
        email
    )
}