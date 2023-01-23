package com.vylo.main.followmain.followfragment.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.PagingUseCaseImpl
import com.vylo.main.followmain.followfragment.data.repository.FollowRepository
import com.vylo.main.followmain.followfragment.data.repository.impl.FollowRepositoryImp
import com.vylo.main.followmain.followfragment.domain.usecase.FollowUseCase
import com.vylo.main.followmain.followfragment.domain.usecase.impl.FollowMapperUseCaseImpl
import com.vylo.main.followmain.followfragment.domain.usecase.impl.FollowUseCaseImpl
import com.vylo.main.followmain.followfragment.presentation.viewmodel.FollowViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val followModule = module {

    single<FollowRepository> { FollowRepositoryImp(androidContext()) }
    single<FollowUseCase> { FollowUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        FollowViewModel(
            get(),
            get(),
            FollowMapperUseCaseImpl(com.vylo.main.followmain.followfragment.data.Mapper(Gson())),
            FollowMapperUseCaseImpl(com.vylo.main.followmain.followfragment.data.Mapper(Gson())),
            androidApplication()
        )
    }
}