package com.vylo.signup.signupcreateusername.domain.usecase.impl

import com.vylo.signup.signupcreateusername.data.repository.SignUpUsernameRepository
import com.vylo.signup.signupcreateusername.domain.entity.request.DraftUserName
import com.vylo.signup.signupcreateusername.domain.usecase.SignUpUsernameUseCase

class SignUpUsernameUseCaseImpl(
    private val signUpUsernameRepository: SignUpUsernameRepository
) : SignUpUsernameUseCase {

    override suspend fun invokeUsername(draftUsername: DraftUserName) =
        signUpUsernameRepository.checkUsername(draftUsername)

    override fun createUsernameData(userName: String) =
        DraftUserName(
        userName
    )
}