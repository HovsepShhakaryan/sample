package com.vylo.resetpassword.forgotpasscode.domain.modul

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.ValidationUseCaseImpl
import com.vylo.common.validation.Validation
import com.vylo.resetpassword.forgotpasscode.data.repository.ForgotPassCodeRepository
import com.vylo.resetpassword.forgotpasscode.data.repository.impl.ForgotPassCodeRepositoryImpl
import com.vylo.resetpassword.forgotpasscode.domain.usecase.ForgotPassCodeUseCase
import com.vylo.resetpassword.forgotpasscode.domain.usecase.impl.ForgotPassCodeUseCaseImpl
import com.vylo.resetpassword.forgotpasscode.presentation.viewmodel.ForgotPassCodeFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val forgotPassCodeModule = module {

    single<ValidationUseCase> { ValidationUseCaseImpl(Validation(androidContext())) }
    single<ForgotPassCodeRepository> { ForgotPassCodeRepositoryImpl(androidContext()) }
    single<ForgotPassCodeUseCase> { ForgotPassCodeUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        ForgotPassCodeFragmentViewModel(
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}