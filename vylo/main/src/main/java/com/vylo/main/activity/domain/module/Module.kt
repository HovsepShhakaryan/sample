package com.vylo.main.activity.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.activity.data.repository.NotificationRepository
import com.vylo.main.activity.data.repository.impl.NotificationRepositoryImpl
import com.vylo.main.activity.domain.usecase.NotificationTokenUseCase
import com.vylo.main.activity.domain.usecase.impl.NotificationTokenUseCaseImpl
import com.vylo.main.activity.presentation.viewmodel.MainFlowActivityViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainActivityModule = module {
    viewModel {
        MainFlowActivityViewModel(
            NotificationTokenUseCaseImpl(NotificationRepositoryImpl(androidContext())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            androidApplication()
        )
    }
}