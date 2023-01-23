package com.vylo.resetpassword.forgotpassemail.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.resetpassword.forgotpassemail.data.endpoint.ForgotPassApi
import com.vylo.resetpassword.forgotpassemail.data.repository.ForgotPassRepository
import com.vylo.resetpassword.forgotpassemail.domain.entity.request.SendEmail
import org.koin.java.KoinJavaComponent

class ForgotPassRepositoryImpl(
    context: Context
) : BaseRepo(), ForgotPassRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(ForgotPassApi::class.java)

    override suspend fun sendEmail(sendEmail: SendEmail) =
        safeApiCall { client.sendEmail(sendEmail) }
}