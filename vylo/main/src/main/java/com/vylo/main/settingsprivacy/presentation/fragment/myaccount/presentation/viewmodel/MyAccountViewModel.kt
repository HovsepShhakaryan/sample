package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.domain.usecase.MyProfileUseCase
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.domain.usecase.MyAccountUseCase
import kotlinx.coroutines.launch

class MyAccountViewModel(
    private val useCase: MyAccountUseCase,
    private val myProfUseCase: MyProfileUseCase,
    application: Application
) : AndroidViewModel(application) {

    fun removeToken() {
        viewModelScope.launch {
            useCase.removeRefreshToken()
            useCase.removeToken()
        }
    }

    fun removeMyGlobalId() {
        viewModelScope.launch {
            myProfUseCase.removeMyGlobalId()
        }
    }

}