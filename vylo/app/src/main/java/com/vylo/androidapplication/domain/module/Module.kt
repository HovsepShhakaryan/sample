package com.vylo.androidapplication.domain.module

import com.google.gson.Gson
import com.vylo.androidapplication.data.repository.impl.VersionRepositoryImpl
import com.vylo.androidapplication.domain.usecase.impl.VersionUseCaseImpl
import com.vylo.androidapplication.presentation.viewmodel.StartActivityViewModel
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val versionModule = module {

    viewModel {
        StartActivityViewModel(
            VersionUseCaseImpl(VersionRepositoryImpl(androidContext())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            androidApplication()
        )
    }
}