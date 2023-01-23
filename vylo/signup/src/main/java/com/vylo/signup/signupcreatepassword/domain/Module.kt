package com.vylo.signup.signupcreatepassword.domain

import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.common.domain.usecase.impl.ValidationUseCaseImpl
import com.vylo.common.validation.Validation
import com.vylo.signup.signupcreatepassword.presentation.viewmodel.SignUpCreatePasswordFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signUpCreatePasswordModule = module {

    single<ValidationUseCase> { ValidationUseCaseImpl(Validation(androidContext())) }
    viewModel{
        SignUpCreatePasswordFragmentViewModel(get())
    }
}