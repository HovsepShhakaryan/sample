package com.vylo.signup.signupinputemail.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.signup.signupinputemail.domain.entity.request.DraftEmail
import com.vylo.signup.signupinputemail.domain.entity.response.DraftEmailData

interface SignUpEmailUseCase {
    suspend fun invokeEmail(draftEmail: DraftEmail): Resource<DraftEmailData>
    fun createEmailData(email: String): DraftEmail
}