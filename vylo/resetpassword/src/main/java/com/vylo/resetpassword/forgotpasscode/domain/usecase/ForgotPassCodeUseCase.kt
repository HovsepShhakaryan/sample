package com.vylo.resetpassword.forgotpasscode.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.resetpassword.forgotpasscode.domain.entity.ConfirmPass

interface ForgotPassCodeUseCase {
    suspend fun invokeCodeConfirm(confirmPass: ConfirmPass): Resource<ConfirmPass>
    fun createConfirmModel(token: String): ConfirmPass
    fun getCode(code: String)
    fun getNewPassword(password: String)
    fun getConfirmNewPassword(password: String)
}