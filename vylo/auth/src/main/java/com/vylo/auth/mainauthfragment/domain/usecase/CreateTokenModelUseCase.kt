package com.vylo.auth.mainauthfragment.domain.usecase

import com.vylo.auth.mainauthfragment.domain.entity.request.Social
import com.vylo.auth.mainauthfragment.domain.entity.request.SocialItem
import com.vylo.common.util.enums.TokenType

interface CreateTokenModelUseCase {
    fun createSocialTokenModel(token: String, type: TokenType): SocialItem
}