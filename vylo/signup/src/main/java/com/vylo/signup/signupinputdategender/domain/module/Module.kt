package com.vylo.signup.signupinputdategender.domain.module

import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.common.domain.usecase.impl.ValidationUseCaseImpl
import com.vylo.common.validation.Validation
import com.vylo.signup.signupinputdategender.presentation.viewmodel.SignUpGenderFragmentViewModel
import com.vylo.signup.signupinputemail.presentation.viewmodel.SignUpWithEmailFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signUpInputGenderModule = module {

    single<ValidationUseCase> { ValidationUseCaseImpl(Validation(androidContext())) }
    viewModel{
        SignUpGenderFragmentViewModel(get())
    }
}