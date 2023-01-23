package com.vylo.signup.signupcreateusername.data.endpoint

import com.vylo.signup.signupcreateusername.domain.entity.request.DraftUserName
import com.vylo.signup.signupcreateusername.domain.entity.response.DraftUserNameData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpUsernameApi {

    companion object {
        const val VALID_USER = "api/is_valid_username"
    }

    @POST(VALID_USER)
    suspend fun checkUsername(
        @Body draftUserName: DraftUserName
    ): Response<DraftUserNameData>
}