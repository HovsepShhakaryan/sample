package com.vylo.signup.signupdonotmissout.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.signup.signupdonotmissout.data.repository.DoNotMissOutRepository
import com.vylo.signup.signupdonotmissout.data.repository.impl.DoNotMissOutRepositoryImpl
import com.vylo.signup.signupdonotmissout.domain.usecase.DoNotMissOutMapperUseCase
import com.vylo.signup.signupdonotmissout.domain.usecase.DoNotMissOutUseCase
import com.vylo.signup.signupdonotmissout.domain.usecase.impl.DoNotMissOutMapperUseCaseImpl
import com.vylo.signup.signupdonotmissout.domain.usecase.impl.DoNotMissOutUseCaseImpl
import com.vylo.signup.signupdonotmissout.presentation.viewmodel.SignUpDoNotMissOutFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signUpDoNotMissOutModule = module {
    single<DoNotMissOutRepository> { DoNotMissOutRepositoryImpl(androidApplication()) }
    single<DoNotMissOutUseCase> { DoNotMissOutUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }
    single<DoNotMissOutMapperUseCase> {
        DoNotMissOutMapperUseCaseImpl(
            com.vylo.signup.signupdonotmissout.data.Mapper(
                Gson()
            )
        )
    }

    viewModel {
        SignUpDoNotMissOutFragmentViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
}