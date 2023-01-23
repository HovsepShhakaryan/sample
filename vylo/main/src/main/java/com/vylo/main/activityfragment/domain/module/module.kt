package com.vylo.main.activityfragment.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.activityfragment.data.repository.ActivityRepository
import com.vylo.main.activityfragment.data.repository.impl.ActivityRepositoryImp
import com.vylo.main.activityfragment.domain.usecase.ActivityUseCase
import com.vylo.main.activityfragment.domain.usecase.impl.ActivityMapperUseCaseImpl
import com.vylo.main.activityfragment.domain.usecase.impl.ActivityUseCaseImpl
import com.vylo.main.activityfragment.presentation.viewmodel.ActivityViewModel
import com.vylo.main.component.kebab.data.repository.KebabRepository
import com.vylo.main.component.kebab.data.repository.impl.KebabRepositoryImpl
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.component.kebab.domain.usecase.impl.KebabUseCaseImpl
import com.vylo.main.following.data.repository.PublisherRepository
import com.vylo.main.following.data.repository.impl.PublisherRepositoryImpl
import com.vylo.main.following.domain.usecase.PublisherUseCase
import com.vylo.main.following.domain.usecase.impl.PublisherUseCaseImpl
import com.vylo.main.profilefragment.data.repository.ProfileRepository
import com.vylo.main.profilefragment.data.repository.impl.ProfileRepositoryImpl
import com.vylo.main.profilefragment.domain.usecase.ProfileUseCase
import com.vylo.main.profilefragment.domain.usecase.impl.ProfileUseCaseImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityModule = module {

    single<ActivityRepository> { ActivityRepositoryImp(androidContext()) }
    single<ActivityUseCase> { ActivityUseCaseImpl(get()) }

    single<KebabRepository> { KebabRepositoryImpl(androidContext()) }
    single<KebabUseCase> { KebabUseCaseImpl(get()) }

    single<PublisherRepository> { PublisherRepositoryImpl(androidContext()) }
    single<PublisherUseCase> { PublisherUseCaseImpl(get()) }

    single<ProfileRepository> { ProfileRepositoryImpl(androidContext()) }
    single<ProfileUseCase> { ProfileUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        ActivityViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            ActivityMapperUseCaseImpl(com.vylo.main.homefragment.data.Mapper(Gson())),
            androidApplication()
        )
    }
}