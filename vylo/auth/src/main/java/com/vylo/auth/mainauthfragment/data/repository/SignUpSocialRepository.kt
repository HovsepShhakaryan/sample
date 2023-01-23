package com.vylo.auth.mainauthfragment.data.repository

import com.vylo.auth.mainauthfragment.domain.entity.request.SocialItem
import com.vylo.auth.mainauthfragment.domain.entity.response.SocialData
import com.vylo.common.api.Resource
import com.vylo.common.util.enums.TokenType

interface SignUpSocialRepository {

    suspend fun signInSocial(
        type: TokenType,
        social: SocialItem
    ): Resource<SocialData>
}