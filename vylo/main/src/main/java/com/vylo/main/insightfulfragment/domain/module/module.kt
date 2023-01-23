package com.vylo.main.insightfulfragment.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.PagingUseCaseImpl
import com.vylo.main.component.kebab.data.repository.KebabRepository
import com.vylo.main.component.kebab.data.repository.impl.KebabRepositoryImpl
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.component.kebab.domain.usecase.impl.KebabUseCaseImpl
import com.vylo.main.insightfulfragment.data.repository.InsightfulRepository
import com.vylo.main.insightfulfragment.data.repository.impl.InsightfulRepositoryImp
import com.vylo.main.insightfulfragment.domain.usecase.InsightfulUseCase
import com.vylo.main.insightfulfragment.domain.usecase.impl.InsightfulMapperUseCaseImpl
import com.vylo.main.insightfulfragment.domain.usecase.impl.InsightfulUseCaseImpl
import com.vylo.main.insightfulfragment.presentation.viewmodel.InsightfulViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val insightfulModule = module {

    single<InsightfulRepository> { InsightfulRepositoryImp(androidContext()) }
    single<InsightfulUseCase> { InsightfulUseCaseImpl(get()) }

    single<KebabRepository> { KebabRepositoryImpl(androidContext()) }
    single<KebabUseCase> { KebabUseCaseImpl(get()) }

    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        InsightfulViewModel(
            get(),
            get(),
            get(),
            InsightfulMapperUseCaseImpl(com.vylo.main.insightfulfragment.data.Mapper(Gson())),
            androidApplication()
        )
    }
}