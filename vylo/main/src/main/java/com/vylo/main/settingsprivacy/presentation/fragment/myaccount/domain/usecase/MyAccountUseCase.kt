package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.domain.usecase

interface MyAccountUseCase {
    suspend fun removeRefreshToken()
    suspend fun removeToken()
}