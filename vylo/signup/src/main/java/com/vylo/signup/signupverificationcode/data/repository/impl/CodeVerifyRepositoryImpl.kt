package com.vylo.signup.signupverificationcode.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.signup.signupverificationcode.data.endpoint.CodeVerifyApi
import com.vylo.signup.signupverificationcode.data.repository.CodeVerifyRepository
import com.vylo.signup.signupverificationcode.domain.entity.request.DraftCode
import org.koin.java.KoinJavaComponent

class CodeVerifyRepositoryImpl(
    context: Context
) : BaseRepo(), CodeVerifyRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(CodeVerifyApi::class.java)

    override suspend fun verifyCode(code: String, draftCode: DraftCode) =
        safeApiCall { client.verifyCode(code, draftCode) }
}