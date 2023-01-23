package com.vylo.auth.mainauthfragment.domain.usecase

import com.vylo.auth.mainauthfragment.domain.entity.request.SocialItem
import com.vylo.auth.mainauthfragment.domain.entity.response.SocialData
import com.vylo.common.api.Resource
import com.vylo.common.util.enums.TokenType

interface SignUpSocialUseCase {

    suspend fun invokeSignInSocial(
        type: TokenType,
        social: SocialItem
    ): Resource<SocialData>
}