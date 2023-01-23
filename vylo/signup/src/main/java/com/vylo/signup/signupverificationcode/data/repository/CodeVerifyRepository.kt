package com.vylo.signup.signupverificationcode.data.repository

import com.vylo.common.api.Resource
import com.vylo.signup.signupverificationcode.domain.entity.request.DraftCode
import com.vylo.signup.signupverificationcode.domain.entity.response.DraftCodeData

interface CodeVerifyRepository {

    suspend fun verifyCode(
        code: String,
        draftCode: DraftCode
    ): Resource<DraftCodeData>
}