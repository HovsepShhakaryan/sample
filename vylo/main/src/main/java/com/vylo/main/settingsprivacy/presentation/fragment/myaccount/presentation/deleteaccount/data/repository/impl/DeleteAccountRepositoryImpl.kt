package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.data.endpoint.DeleteAccountApi
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.data.repository.DeleteAccountRepository
import org.koin.java.KoinJavaComponent

class DeleteAccountRepositoryImpl(
    context: Context
) : BaseRepo(), DeleteAccountRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(DeleteAccountApi::class.java)

    override suspend fun deleteProfile() = safeApiCall { client.deleteProfile() }
}