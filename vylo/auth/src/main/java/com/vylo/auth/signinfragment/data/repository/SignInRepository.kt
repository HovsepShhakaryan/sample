package com.vylo.auth.signinfragment.data.repository

import com.vylo.auth.signinfragment.domain.entity.request.SignIn
import com.vylo.common.domain.entity.responce.Tokens
import com.vylo.common.api.Resource

interface SignInRepository {
    suspend fun postSignInCall(signIn: SignIn): Resource<Tokens>
}