package com.vylo.resetpassword.forgotpasscode.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.resetpassword.forgotpasscode.data.endpoint.ForgotPassCodeApi
import com.vylo.resetpassword.forgotpasscode.data.repository.ForgotPassCodeRepository
import com.vylo.resetpassword.forgotpasscode.domain.entity.ConfirmPass
import org.koin.java.KoinJavaComponent

class ForgotPassCodeRepositoryImpl(
    context: Context
) : BaseRepo(), ForgotPassCodeRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(ForgotPassCodeApi::class.java)

    override suspend fun sendPasswordConfirmation(confirmPass: ConfirmPass) =
        safeApiCall { client.sendPasswordConfirmation(confirmPass) }

}