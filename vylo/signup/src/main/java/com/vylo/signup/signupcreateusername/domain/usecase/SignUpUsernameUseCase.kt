package com.vylo.signup.signupcreateusername.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.signup.signupcreateusername.domain.entity.request.DraftUserName
import com.vylo.signup.signupcreateusername.domain.entity.response.DraftUserNameData

interface SignUpUsernameUseCase {
    suspend fun invokeUsername(draftUsername: DraftUserName): Resource<DraftUserNameData>
    fun createUsernameData(userName: String): DraftUserName
}