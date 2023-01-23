package com.vylo.auth.mainauthfragment.domain.usecase.impl

import com.vylo.auth.mainauthfragment.domain.usecase.TokensUseCase
import com.vylo.common.util.SharedPreferenceData

class TokensUseCaseImpl(
    private val sharedPreferenceData: SharedPreferenceData
) : TokensUseCase {

    override suspend fun saveRefreshToken(refreshToken: String) {
        sharedPreferenceData.saveRefreshToken(refreshToken)
    }

    override suspend fun saveToken(token: String) {
        sharedPreferenceData.saveToken(token)
    }

    override suspend fun savePartialToken(partialToken: String) {
        sharedPreferenceData.savePartialToken(partialToken)
    }
}