package com.vylo.main.trending.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.MyProfileUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.MyProfileUseCaseImpl
import com.vylo.common.util.SharedPreferenceData
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.CategoryUseCase
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.impl.CategoryUseCaseImpl
import com.vylo.main.component.kebab.data.repository.KebabRepository
import com.vylo.main.component.kebab.data.repository.impl.KebabRepositoryImpl
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.component.kebab.domain.usecase.impl.KebabUseCaseImpl
import com.vylo.main.homefragment.domain.usecase.impl.FeedMapperUseCaseImpl
import com.vylo.main.trending.data.repository.TrendingRepository
import com.vylo.main.trending.data.repository.impl.TrendingRepositoryImp
import com.vylo.main.trending.domain.usecase.TrendingUseCase
import com.vylo.main.trending.domain.usecase.impl.TrendingUseCaseImpl
import com.vylo.main.trending.presentation.viewmodel.TrendingViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val trendingModule = module {

    single<TrendingRepository> { TrendingRepositoryImp(androidContext()) }
    single<KebabRepository> { KebabRepositoryImpl(androidContext()) }
    single<TrendingUseCase> { TrendingUseCaseImpl(get(), SharedPreferenceData(androidContext())) }
    single<MyProfileUseCase> { MyProfileUseCaseImpl(SharedPreferenceData(androidContext())) }
    single<KebabUseCase> { KebabUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }
    single<CategoryUseCase> { CategoryUseCaseImpl(get()) }

    viewModel {
        TrendingViewModel(
            get(),
            get(),
            get(),
            get(),
            FeedMapperUseCaseImpl(com.vylo.main.homefragment.data.Mapper(Gson())),
            get(),
            androidApplication()
        )
    }
}