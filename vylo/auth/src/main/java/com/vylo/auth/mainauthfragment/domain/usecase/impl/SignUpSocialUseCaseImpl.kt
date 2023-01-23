package com.vylo.auth.mainauthfragment.domain.usecase.impl

import com.vylo.auth.mainauthfragment.data.repository.SignUpSocialRepository
import com.vylo.auth.mainauthfragment.domain.entity.request.Social
import com.vylo.auth.mainauthfragment.domain.entity.request.SocialItem
import com.vylo.auth.mainauthfragment.domain.usecase.SignUpSocialUseCase
import com.vylo.common.util.enums.TokenType

class SignUpSocialUseCaseImpl(
    private val signUpSocialRepository: SignUpSocialRepository
) : SignUpSocialUseCase {

    override suspend fun invokeSignInSocial(type: TokenType, social: SocialItem) =
        signUpSocialRepository.signInSocial(type, social)
}