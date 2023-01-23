package com.vylo.main.homefragment.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.MyProfileUseCaseImpl
import com.vylo.common.util.SharedPreferenceData
import com.vylo.main.component.kebab.data.repository.impl.KebabRepositoryImpl
import com.vylo.main.component.kebab.domain.usecase.impl.KebabUseCaseImpl
import com.vylo.main.homefragment.data.repository.impl.FeedRepositoryImpl
import com.vylo.main.homefragment.domain.usecase.impl.FeedMapperUseCaseImpl
import com.vylo.main.homefragment.domain.usecase.impl.FeedUseCaseImpl
import com.vylo.main.homefragment.presentation.viewmodel.HomeFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel {
        HomeFragmentViewModel(
            FeedUseCaseImpl(
                FeedRepositoryImpl(androidContext()),
                SharedPreferenceData(androidContext())
            ),
            KebabUseCaseImpl(KebabRepositoryImpl(androidApplication())),
            MyProfileUseCaseImpl((SharedPreferenceData(androidContext()))),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            FeedMapperUseCaseImpl(com.vylo.main.homefragment.data.Mapper(Gson())),
            androidApplication()
        )
    }
}