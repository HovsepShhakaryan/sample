package com.vylo.signup.signupverificationcode.domain.usecase.impl

import com.vylo.signup.signupverificationcode.data.repository.CodeVerifyRepository
import com.vylo.signup.signupverificationcode.domain.entity.request.DraftCode
import com.vylo.signup.signupverificationcode.domain.usecase.CodeVerifyUseCase

class CodeVerifyUseCaseImpl(
    private val codeVerifyRepository: CodeVerifyRepository
) : CodeVerifyUseCase {

    override suspend fun invokeCode(code: String, draftCode: DraftCode) =
        codeVerifyRepository.verifyCode(code, draftCode)

    override fun createEmailData(email: String) =
        DraftCode(
            email
        )
}