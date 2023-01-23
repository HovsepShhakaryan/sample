package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.data.repository

import com.vylo.common.api.Resource

interface DeleteAccountRepository {
    suspend fun deleteProfile(): Resource<Unit>
}