package com.vylo.main.newsstand.presentation.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.MyProfileUseCaseImpl
import com.vylo.common.util.SharedPreferenceData
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.CategoryUseCase
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.impl.CategoryMapperUseCaseImpl
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.impl.CategoryUseCaseImpl
import com.vylo.main.component.kebab.data.repository.impl.KebabRepositoryImpl
import com.vylo.main.component.kebab.domain.usecase.impl.KebabUseCaseImpl
import com.vylo.main.homefragment.domain.usecase.impl.FeedMapperUseCaseImpl
import com.vylo.main.newsstand.data.repository.impl.NewsFeedRepositoryImpl
import com.vylo.main.newsstand.domain.usecase.impl.NewsFeedMapperUseCaseImpl
import com.vylo.main.newsstand.domain.usecase.impl.NewsFeedUseCaseImpl
import com.vylo.main.newsstand.presentation.viewmodel.NewsstandViewModel
import com.vylo.main.trending.domain.usecase.TrendingUseCase
import com.vylo.main.trending.domain.usecase.impl.TrendingUseCaseImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsstandModule = module {

    single<CategoryUseCase> { CategoryUseCaseImpl(get()) }

    viewModel {
        NewsstandViewModel(
            NewsFeedUseCaseImpl(NewsFeedRepositoryImpl(androidApplication())),
            KebabUseCaseImpl(KebabRepositoryImpl(androidApplication())),
            MyProfileUseCaseImpl((SharedPreferenceData(androidContext()))),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            NewsFeedMapperUseCaseImpl(com.vylo.main.newsstand.data.Mapper(Gson())),
            FeedMapperUseCaseImpl(com.vylo.main.homefragment.data.Mapper(Gson())),
            TrendingUseCaseImpl(get(), SharedPreferenceData(androidContext())),
            get(),
            androidApplication()
        )
    }
}