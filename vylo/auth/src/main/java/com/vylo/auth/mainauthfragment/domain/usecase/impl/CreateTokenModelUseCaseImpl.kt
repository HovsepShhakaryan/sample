package com.vylo.auth.mainauthfragment.domain.usecase.impl

import com.vylo.auth.mainauthfragment.domain.entity.request.SocialItem
import com.vylo.auth.mainauthfragment.domain.usecase.CreateTokenModelUseCase
import com.vylo.common.util.enums.TokenType

class CreateTokenModelUseCaseImpl : CreateTokenModelUseCase {

    override fun createSocialTokenModel(token: String, type: TokenType) = when (type) {
            TokenType.GOOGLE -> SocialItem(
                    idToken = token
                )

            TokenType.FACEBOOK -> SocialItem(
                    accessToken = token
                )

        }

}