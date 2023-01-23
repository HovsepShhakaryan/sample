package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.data.endpoint.UpdatePasswordApi
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.data.repository.UpdatePasswordRepository
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.entity.request.PasswordRequest
import org.koin.java.KoinJavaComponent

class UpdatePasswordRepositoryImpl(
    context: Context
) : BaseRepo(), UpdatePasswordRepository {

    private val apiColumn: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val clientColumn = apiColumn.getInstance(context).create(UpdatePasswordApi::class.java)

    override suspend fun updatePassword(request: PasswordRequest) =
        safeApiCall { clientColumn.updatePassword(request) }
}