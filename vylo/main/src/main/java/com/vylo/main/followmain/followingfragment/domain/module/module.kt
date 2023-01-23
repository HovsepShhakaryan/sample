package com.vylo.main.followmain.followingfragment.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.globalsearch.searchprofiles.domain.usecase.impl.SearchProfileMapperUseCaseImpl
import com.vylo.main.followmain.followingfragment.data.repository.FollowingRepository
import com.vylo.main.followmain.followingfragment.data.repository.impl.FollowingRepositoryImp
import com.vylo.main.followmain.followingfragment.domain.usecase.FollowingUseCase
import com.vylo.main.followmain.followingfragment.domain.usecase.impl.FollowingUseCaseImpl
import com.vylo.main.followmain.followingfragment.presentation.viewmodel.FollowingViewModel
import com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.SearchProfileMapperUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val followingModule = module {

    single<FollowingRepository> { FollowingRepositoryImp(androidContext()) }
    single<FollowingUseCase> { FollowingUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }
    single<SearchProfileMapperUseCase> { SearchProfileMapperUseCaseImpl(
        com.vylo.main.globalsearchmain.searchprofiles.data.repository.Mapper(
            Gson()
        )
    ) }

    viewModel {
        FollowingViewModel(
            get(),
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}