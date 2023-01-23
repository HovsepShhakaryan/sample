package com.vylo.main.webfragment.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.component.kebab.data.repository.KebabRepository
import com.vylo.main.component.kebab.data.repository.impl.KebabRepositoryImpl
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.component.kebab.domain.usecase.impl.KebabUseCaseImpl
import com.vylo.main.videofragment.data.repository.VideoRepository
import com.vylo.main.videofragment.data.repository.impl.VideoRepositoryImpl
import com.vylo.main.videofragment.domain.usecase.VideoUseCase
import com.vylo.main.videofragment.domain.usecase.impl.VideoUseCaseImpl
import com.vylo.main.webfragment.presentation.viewmodel.WebViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val webModule = module {
    single<VideoRepository> { VideoRepositoryImpl(androidContext()) }
    single<VideoUseCase> { VideoUseCaseImpl(get()) }

    single<KebabRepository> { KebabRepositoryImpl(androidContext()) }
    single<KebabUseCase> { KebabUseCaseImpl(get()) }

    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        WebViewModel(
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}