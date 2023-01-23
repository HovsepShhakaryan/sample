package com.vylo.signup.signupverificationcode.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.signup.signupverificationcode.domain.entity.request.DraftCode
import com.vylo.signup.signupverificationcode.domain.entity.response.DraftCodeData

interface CodeVerifyUseCase {
    suspend fun invokeCode(code: String, draftCode: DraftCode): Resource<DraftCodeData>
    fun createEmailData(email: String): DraftCode
}