package com.vylo.main.profilefragment.domain.module

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
import com.vylo.main.following.data.repository.impl.PublisherRepositoryImpl
import com.vylo.main.following.domain.usecase.impl.PublisherUseCaseImpl
import com.vylo.main.profilefragment.data.repository.ProfileRepository
import com.vylo.main.profilefragment.data.repository.impl.ProfileRepositoryImpl
import com.vylo.main.profilefragment.domain.usecase.ProfileUseCase
import com.vylo.main.profilefragment.domain.usecase.impl.ProfileMapperUseCaseImpl
import com.vylo.main.profilefragment.domain.usecase.impl.ProfileUseCaseImpl
import com.vylo.main.profilefragment.presentation.viewmodel.EditProfileViewModel
import com.vylo.main.profilefragment.presentation.viewmodel.ProfileViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {

    single<PagingUseCase> { PagingUseCaseImpl() }
    single<KebabRepository> { KebabRepositoryImpl(androidContext()) }
    single<KebabUseCase> { KebabUseCaseImpl(get()) }
    single<MyProfileUseCase> { MyProfileUseCaseImpl(SharedPreferenceData(androidContext())) }

    viewModel {
        ProfileViewModel(
            ProfileUseCaseImpl(ProfileRepositoryImpl(androidApplication())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            get(),
            ProfileMapperUseCaseImpl(com.vylo.main.profilefragment.data.Mapper(Gson())),
            PublisherUseCaseImpl(PublisherRepositoryImpl(androidApplication())),
            get(),
            androidApplication()
        )
    }
}

val editProfileModule = module {
    single<ProfileRepository> { ProfileRepositoryImpl(androidApplication()) }
    single<ProfileUseCase> { ProfileUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        EditProfileViewModel(
            get(),
            get(),
            androidApplication()
        )
    }
}