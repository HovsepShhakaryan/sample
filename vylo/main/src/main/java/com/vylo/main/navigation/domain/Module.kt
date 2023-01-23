package com.vylo.main.navigation.domain

import com.google.gson.Gson
import com.vylo.main.responsemain.createvideo.data.repository.impl.VideoRepositoryImpl
import com.hovsep.camera.createvideo.domain.usecase.impl.GetParametersFromUrlUseCaseImpl
import com.vylo.main.responsemain.createvideo.domain.usecase.impl.PresignedVideoUploadUseCaseImpl
import com.vylo.main.navigation.presentation.viewmodel.NavigationFragmentsViewModel
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cameraModule = module {
    viewModel {
        NavigationFragmentsViewModel(
            PresignedVideoUploadUseCaseImpl(VideoRepositoryImpl(androidContext())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            GetParametersFromUrlUseCaseImpl(),
            androidApplication()
        )
    }

}