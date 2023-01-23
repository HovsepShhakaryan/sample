package com.vylo.main.globalsearchmain.searchstartscreen.domain.modul

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.data.repository.impl.RecentRepositoryImpl
import com.vylo.common.domain.usecase.impl.RecentUseCaseImpl
import com.vylo.main.globalsearchmain.searchstartscreen.data.repository.SearchStartRepository
import com.vylo.main.globalsearchmain.searchstartscreen.data.repository.impl.SearchStartRepositoryImpl
import com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.SearchStartMapperUseCase
import com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.SearchStartUseCase
import com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.impl.SearchStartMapperUseCaseImpl
import com.vylo.main.globalsearchmain.searchstartscreen.domain.usecase.impl.SearchStartUseCaseImpl
import com.vylo.main.globalsearchmain.searchstartscreen.presentation.viewmodel.SearchStartFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchStartModule = module {

    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }
    single<SearchStartMapperUseCase> { SearchStartMapperUseCaseImpl(com.vylo.main.globalsearchmain.searchstartscreen.data.Mapper(Gson())) }
    single<SearchStartRepository> { SearchStartRepositoryImpl(androidContext()) }
    single<SearchStartUseCase> { SearchStartUseCaseImpl(get()) }

    viewModel {
        SearchStartFragmentViewModel(
            RecentUseCaseImpl(
                RecentRepositoryImpl(androidContext()),
                Mapper(Gson())
            ),
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}