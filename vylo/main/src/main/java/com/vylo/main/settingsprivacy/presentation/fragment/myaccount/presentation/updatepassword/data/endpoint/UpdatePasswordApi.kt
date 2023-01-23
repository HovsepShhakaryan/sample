package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.data.endpoint

import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.entity.request.PasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UpdatePasswordApi {

    @POST(PASSWORD)
    suspend fun updatePassword(
        @Body request: PasswordRequest
    ): Response<Any>

    companion object {
        private const val PASSWORD = "auth/users/set_password/"
    }
}