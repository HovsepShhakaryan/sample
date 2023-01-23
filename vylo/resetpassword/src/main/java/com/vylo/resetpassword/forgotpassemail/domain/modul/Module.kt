package com.vylo.resetpassword.forgotpassemail.domain.modul

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.ValidationUseCaseImpl
import com.vylo.common.validation.Validation
import com.vylo.resetpassword.forgotpassemail.data.repository.ForgotPassRepository
import com.vylo.resetpassword.forgotpassemail.data.repository.impl.ForgotPassRepositoryImpl
import com.vylo.resetpassword.forgotpassemail.domain.usecase.ForgotPassUseCase
import com.vylo.resetpassword.forgotpassemail.domain.usecase.impl.ForgotPassUseCaseImpl
import com.vylo.resetpassword.forgotpassemail.presentation.viewmodel.ForgotPassEmailFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val forgotPassEmailModule = module {

    single<ValidationUseCase> { ValidationUseCaseImpl(Validation(androidContext())) }
    single<ForgotPassRepository> { ForgotPassRepositoryImpl(androidContext()) }
    single<ForgotPassUseCase> { ForgotPassUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        ForgotPassEmailFragmentViewModel(
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}