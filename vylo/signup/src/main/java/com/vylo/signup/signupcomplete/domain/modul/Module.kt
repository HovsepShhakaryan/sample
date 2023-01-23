package com.vylo.signup.signupcomplete.domain.modul

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.util.SharedPreferenceData
import com.vylo.signup.signupcomplete.data.repository.SignUpRepository
import com.vylo.signup.signupcomplete.data.repository.impl.SignUpRepositoryImpl
import com.vylo.signup.signupcomplete.domain.usecase.SignUpUseCase
import com.vylo.signup.signupcomplete.domain.usecase.impl.SignUpUseCaseImpl
import com.vylo.signup.signupcomplete.presentation.viewmodel.SignUpCompleteFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signUpModule = module {

    single<SignUpRepository> { SignUpRepositoryImpl(androidContext()) }
    single<SignUpUseCase> {
        SignUpUseCaseImpl(
            get(),
            SharedPreferenceData(androidContext())
        )
    }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        SignUpCompleteFragmentViewModel(
            get(),
            get(),
            androidApplication()
        )
    }
}