package com.vylo.resetpassword.forgotpasscode.data.repository

import com.vylo.common.api.Resource
import com.vylo.resetpassword.forgotpasscode.domain.entity.ConfirmPass

interface ForgotPassCodeRepository {

    suspend fun sendPasswordConfirmation(
        confirmPass: ConfirmPass
    ): Resource<ConfirmPass>
}