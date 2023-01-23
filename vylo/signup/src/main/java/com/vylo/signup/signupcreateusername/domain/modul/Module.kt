package com.vylo.signup.signupcreateusername.domain.modul

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.ValidationUseCaseImpl
import com.vylo.common.validation.Validation
import com.vylo.signup.signupcreateusername.data.repository.SignUpUsernameRepository
import com.vylo.signup.signupcreateusername.data.repository.impl.SignUpUsernameRepositoryImpl
import com.vylo.signup.signupcreateusername.domain.usecase.SignUpUsernameUseCase
import com.vylo.signup.signupcreateusername.domain.usecase.impl.SignUpUsernameUseCaseImpl
import com.vylo.signup.signupcreateusername.presentation.viewmodel.SignUpCreateUsernameFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signUpChooseUserNameModule = module {

    single<ValidationUseCase> { ValidationUseCaseImpl(Validation(androidContext())) }
    single<SignUpUsernameRepository> { SignUpUsernameRepositoryImpl(androidContext()) }
    single<SignUpUsernameUseCase> { SignUpUsernameUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel{
        SignUpCreateUsernameFragmentViewModel(
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}