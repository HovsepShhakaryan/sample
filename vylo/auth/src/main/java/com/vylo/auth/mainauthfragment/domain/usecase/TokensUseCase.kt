package com.vylo.auth.mainauthfragment.domain.usecase

interface TokensUseCase {

    suspend fun saveRefreshToken(
        refreshToken: String
    )
    suspend fun saveToken(
        token: String
    )
    suspend fun savePartialToken(
        partialToken: String
    )
}