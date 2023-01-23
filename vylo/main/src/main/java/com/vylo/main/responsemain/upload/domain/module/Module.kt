package com.vylo.main.responsemain.upload.domain.module

import com.google.gson.Gson
import com.vylo.main.responsemain.createvideo.data.repository.impl.VideoRepositoryImpl
import com.hovsep.camera.createvideo.domain.usecase.impl.GetParametersFromUrlUseCaseImpl
import com.vylo.main.responsemain.createvideo.domain.usecase.impl.PresignedVideoUploadUseCaseImpl
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.responsemain.upload.data.repository.impl.VideoUploadRepositoryImpl
import com.vylo.main.responsemain.upload.domain.usecase.impl.ColumnNewsUseCaseImpl
import com.vylo.main.responsemain.upload.domain.usecase.impl.VideoUploadUseCaseImpl
import com.vylo.main.responsemain.upload.presentation.viewmodel.UploadFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uploadVideoModule = module {
    viewModel {
        UploadFragmentViewModel(
            VideoUploadUseCaseImpl(VideoUploadRepositoryImpl(androidContext())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            ColumnNewsUseCaseImpl(),
            PresignedVideoUploadUseCaseImpl(VideoRepositoryImpl(androidContext())),
            GetParametersFromUrlUseCaseImpl(),
            androidApplication()
        )
    }
}