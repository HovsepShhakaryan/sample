package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.domain.module

import com.vylo.common.domain.usecase.MyProfileUseCase
import com.vylo.common.domain.usecase.impl.MyProfileUseCaseImpl
import com.vylo.common.util.SharedPreferenceData
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.domain.usecase.MyAccountUseCase
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.domain.usecase.impl.MyAccountUseCaseImpl
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.viewmodel.MyAccountViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myAccountModule = module {
    single { SharedPreferenceData(androidContext()) }
    single<MyAccountUseCase> { MyAccountUseCaseImpl(get()) }
    single<MyProfileUseCase> { MyProfileUseCaseImpl(get()) }

    viewModel {
        MyAccountViewModel(
            get(),
            get(),
            androidApplication()
        )
    }
}