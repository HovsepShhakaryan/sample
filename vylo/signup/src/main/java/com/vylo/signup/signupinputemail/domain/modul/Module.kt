package com.vylo.signup.signupinputemail.domain.modul

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.ValidationUseCaseImpl
import com.vylo.common.validation.Validation
import com.vylo.signup.signupinputemail.data.repository.SignUpEmailRepository
import com.vylo.signup.signupinputemail.data.repository.impl.SignUpEmailRepositoryImpl
import com.vylo.signup.signupinputemail.domain.usecase.SignUpEmailUseCase
import com.vylo.signup.signupinputemail.domain.usecase.impl.SignUpEmailUseCaseImpl
import com.vylo.signup.signupinputemail.presentation.viewmodel.SignUpWithEmailFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signUpInputEmailModule = module {

    single<ValidationUseCase> { ValidationUseCaseImpl(Validation(androidContext())) }
    single<SignUpEmailRepository> { SignUpEmailRepositoryImpl(androidContext()) }
    single<SignUpEmailUseCase> { SignUpEmailUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        SignUpWithEmailFragmentViewModel(
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}