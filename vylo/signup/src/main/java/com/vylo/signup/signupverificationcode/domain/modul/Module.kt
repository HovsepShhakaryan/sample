package com.vylo.signup.signupverificationcode.domain.modul

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.ValidationUseCaseImpl
import com.vylo.common.validation.Validation
import com.vylo.signup.signupverificationcode.data.repository.CodeVerifyRepository
import com.vylo.signup.signupverificationcode.data.repository.impl.CodeVerifyRepositoryImpl
import com.vylo.signup.signupverificationcode.domain.usecase.CodeVerifyUseCase
import com.vylo.signup.signupverificationcode.domain.usecase.impl.CodeVerifyUseCaseImpl
import com.vylo.signup.signupverificationcode.presentation.viewmodel.SignUpVerificationCodeFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signUpCodeVerifyModule = module {

    single<ValidationUseCase> { ValidationUseCaseImpl(Validation(androidContext())) }
    single<CodeVerifyRepository> { CodeVerifyRepositoryImpl(androidContext()) }
    single<CodeVerifyUseCase> { CodeVerifyUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        SignUpVerificationCodeFragmentViewModel(
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}