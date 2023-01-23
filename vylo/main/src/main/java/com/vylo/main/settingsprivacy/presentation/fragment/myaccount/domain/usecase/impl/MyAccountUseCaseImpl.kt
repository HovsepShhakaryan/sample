package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.domain.usecase.impl

import com.vylo.common.util.SharedPreferenceData
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.domain.usecase.MyAccountUseCase

class MyAccountUseCaseImpl(
    private val sharedPreferenceData: SharedPreferenceData
) : MyAccountUseCase {
    override suspend fun removeRefreshToken() {
        sharedPreferenceData.removeRefreshToken()
    }

    override suspend fun removeToken() {
        sharedPreferenceData.removeToken()
    }
}