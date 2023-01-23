package com.vylo.auth.mainauthfragment.domain.modul

import com.google.gson.Gson
import com.vylo.auth.mainauthfragment.data.repository.SignUpSocialRepository
import com.vylo.auth.mainauthfragment.data.repository.impl.SignUpSocialRepositoryImpl
import com.vylo.auth.mainauthfragment.domain.usecase.CreateTokenModelUseCase
import com.vylo.auth.mainauthfragment.domain.usecase.SignUpSocialUseCase
import com.vylo.auth.mainauthfragment.domain.usecase.TokensUseCase
import com.vylo.auth.mainauthfragment.domain.usecase.impl.CreateTokenModelUseCaseImpl
import com.vylo.auth.mainauthfragment.domain.usecase.impl.SignUpSocialUseCaseImpl
import com.vylo.auth.mainauthfragment.domain.usecase.impl.TokensUseCaseImpl
import com.vylo.auth.mainauthfragment.presentation.viewmodel.WelcomeFragmentViewModel
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.util.SharedPreferenceData
import com.vylo.signup.signupcomplete.domain.usecase.SignUpUseCase
import com.vylo.signup.signupcomplete.domain.usecase.impl.SignUpUseCaseImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val welcomeModule = module {

    single<SignUpSocialRepository> { SignUpSocialRepositoryImpl(androidContext()) }
    single<SignUpSocialUseCase> { SignUpSocialUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }
    single<CreateTokenModelUseCase> { CreateTokenModelUseCaseImpl() }
    single<TokensUseCase> { TokensUseCaseImpl(SharedPreferenceData(androidContext())) }

    viewModel {
        WelcomeFragmentViewModel(
            get(),
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}