package com.vylo.resetpassword.forgotpasscode.domain.usecase.impl

import com.vylo.resetpassword.forgotpasscode.data.repository.ForgotPassCodeRepository
import com.vylo.resetpassword.forgotpasscode.domain.entity.ConfirmPass
import com.vylo.resetpassword.forgotpasscode.domain.usecase.ForgotPassCodeUseCase

class ForgotPassCodeUseCaseImpl(
    private val forgotPassCodeRepository: ForgotPassCodeRepository
) : ForgotPassCodeUseCase {

    private var code: String? = null
    private var password: String? = null
    private var confirmPassword: String? = null

    override suspend fun invokeCodeConfirm(confirmPass: ConfirmPass) =
        forgotPassCodeRepository.sendPasswordConfirmation(confirmPass)

    override fun createConfirmModel(token: String) = ConfirmPass(
        token,
        code,
        password,
        confirmPassword
    )

    override fun getCode(code: String) {
        this.code = code.trim()
    }

    override fun getNewPassword(password: String) {
        this.password = password.trim()
    }

    override fun getConfirmNewPassword(password: String) {
        this.confirmPassword = password.trim()
    }
}