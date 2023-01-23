package com.vylo.signup.signupinputemail.data.endpoint

import com.vylo.signup.signupinputemail.domain.entity.request.DraftEmail
import com.vylo.signup.signupinputemail.domain.entity.response.DraftEmailData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpEmailApi {

    companion object {
        const val DRAFT_USER = "api/draft_user"
    }

    @POST(DRAFT_USER)
    suspend fun checkEmail(
        @Body draftEmail: DraftEmail
    ): Response<DraftEmailData>
}