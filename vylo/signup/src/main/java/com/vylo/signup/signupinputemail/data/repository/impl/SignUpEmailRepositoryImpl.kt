package com.vylo.signup.signupinputemail.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.signup.signupinputemail.data.endpoint.SignUpEmailApi
import com.vylo.signup.signupinputemail.data.repository.SignUpEmailRepository
import com.vylo.signup.signupinputemail.domain.entity.request.DraftEmail
import org.koin.java.KoinJavaComponent

class SignUpEmailRepositoryImpl(
    context: Context
) : BaseRepo(), SignUpEmailRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(SignUpEmailApi::class.java)

    override suspend fun checkEmail(draftEmail: DraftEmail) =
        safeApiCall { client.checkEmail(draftEmail) }
}