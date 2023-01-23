package com.vylo.main.videofragment.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.MyProfileUseCase
import com.vylo.common.domain.usecase.PagingUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.MyProfileUseCaseImpl
import com.vylo.common.domain.usecase.impl.PagingUseCaseImpl
import com.vylo.common.util.SharedPreferenceData
import com.vylo.main.component.kebab.data.repository.KebabRepository
import com.vylo.main.component.kebab.data.repository.impl.KebabRepositoryImpl
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.component.kebab.domain.usecase.impl.KebabUseCaseImpl
import com.vylo.main.following.data.repository.PublisherRepository
import com.vylo.main.following.data.repository.impl.PublisherRepositoryImpl
import com.vylo.main.following.domain.usecase.PublisherUseCase
import com.vylo.main.following.domain.usecase.impl.PublisherUseCaseImpl
import com.vylo.main.followmain.followfragment.data.repository.FollowRepository
import com.vylo.main.followmain.followfragment.data.repository.impl.FollowRepositoryImp
import com.vylo.main.followmain.followfragment.domain.usecase.FollowUseCase
import com.vylo.main.followmain.followfragment.domain.usecase.impl.FollowUseCaseImpl
import com.vylo.main.profilefragment.data.repository.ProfileRepository
import com.vylo.main.profilefragment.data.repository.impl.ProfileRepositoryImpl
import com.vylo.main.profilefragment.domain.usecase.ProfileUseCase
import com.vylo.main.profilefragment.domain.usecase.impl.ProfileUseCaseImpl
import com.vylo.main.videofragment.data.repository.VideoRepository
import com.vylo.main.videofragment.data.repository.impl.VideoRepositoryImpl
import com.vylo.main.videofragment.domain.usecase.VideoUseCase
import com.vylo.main.videofragment.domain.usecase.impl.VideoMapperUseCaseImpl
import com.vylo.main.videofragment.domain.usecase.impl.VideoUseCaseImpl
import com.vylo.main.videofragment.presentation.viewmodel.VideoViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val videoModule = module {
    single<KebabRepository> { KebabRepositoryImpl(androidApplication()) }
    single<KebabUseCase> { KebabUseCaseImpl(get()) }

    single<VideoRepository> { VideoRepositoryImpl(androidApplication()) }
    single<VideoUseCase> { VideoUseCaseImpl(get()) }

    single<PublisherRepository> { PublisherRepositoryImpl(androidApplication()) }
    single<PublisherUseCase> { PublisherUseCaseImpl(get()) }

    single<ProfileRepository> { ProfileRepositoryImpl(androidApplication()) }
    single<ProfileUseCase> { ProfileUseCaseImpl(get()) }

    single<FollowRepository> { FollowRepositoryImp(androidApplication()) }
    single<FollowUseCase> { FollowUseCaseImpl(get()) }

    single<MyProfileUseCase> { MyProfileUseCaseImpl(SharedPreferenceData(androidContext())) }

    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    single<PagingUseCase> { PagingUseCaseImpl() }

    viewModel {
        VideoViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            VideoMapperUseCaseImpl(com.vylo.main.videofragment.data.Mapper(Gson())),
            androidApplication()
        )
    }
}