package com.vylo.signup.signupprofileimage.domain.modul

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.signup.signupprofileimage.data.repository.impl.ProfilePickRepositoryImpl
import com.vylo.signup.signupprofileimage.domain.usecase.impl.ProfilePickUseCaseImpl
import com.vylo.signup.signupprofileimage.presentation.viewmodel.SignUpPickUpProfilePictureFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profilePickModule = module {
    viewModel {
        SignUpPickUpProfilePictureFragmentViewModel(
            ProfilePickUseCaseImpl(ProfilePickRepositoryImpl(androidContext())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            androidApplication()
        )
    }
}