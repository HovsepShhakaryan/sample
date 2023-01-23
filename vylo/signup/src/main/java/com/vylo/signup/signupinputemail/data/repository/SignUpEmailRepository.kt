package com.vylo.signup.signupinputemail.data.repository

import com.vylo.common.api.Resource
import com.vylo.signup.signupinputemail.domain.entity.request.DraftEmail
import com.vylo.signup.signupinputemail.domain.entity.response.DraftEmailData

interface SignUpEmailRepository {

    suspend fun checkEmail(
        draftEmail: DraftEmail
    ): Resource<DraftEmailData>
}