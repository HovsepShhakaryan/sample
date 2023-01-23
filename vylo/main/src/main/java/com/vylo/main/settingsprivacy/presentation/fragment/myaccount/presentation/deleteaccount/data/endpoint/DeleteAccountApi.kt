package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.data.endpoint

import retrofit2.Response
import retrofit2.http.DELETE

interface DeleteAccountApi {

    @DELETE(PROFILE)
    suspend fun deleteProfile(): Response<Unit>

    companion object {
        private const val PROFILE = "api/profile"
    }
}