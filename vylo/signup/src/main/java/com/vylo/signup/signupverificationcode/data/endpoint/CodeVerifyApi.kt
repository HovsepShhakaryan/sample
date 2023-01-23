package com.vylo.signup.signupverificationcode.data.endpoint

import com.vylo.signup.signupverificationcode.domain.entity.request.DraftCode
import com.vylo.signup.signupverificationcode.domain.entity.response.DraftCodeData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CodeVerifyApi {

    companion object {
        const val VERIFY_USER = "api/draft_user/{code}"
    }

    @POST(VERIFY_USER)
    suspend fun verifyCode(
        @Path("code") code: String,
        @Body draftCode: DraftCode
    ): Response<DraftCodeData>
}