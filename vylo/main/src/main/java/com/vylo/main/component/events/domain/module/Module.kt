package com.vylo.main.component.events.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.component.events.data.repository.EventRepository
import com.vylo.main.component.events.data.repository.impl.EventRepositoryImpl
import com.vylo.main.component.events.domain.usecase.EventUseCase
import com.vylo.main.component.events.domain.usecase.impl.EventUseCaseImpl
import com.vylo.main.component.events.presentation.EventsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val eventsModule = module {

    single<EventRepository> { EventRepositoryImpl(androidContext()) }
    single<EventUseCase> { EventUseCaseImpl(get()) }

    viewModel {
        EventsViewModel(
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            get(),
            androidApplication()
        )
    }
}