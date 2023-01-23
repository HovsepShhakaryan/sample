package com.vylo.common.domain.module

import com.vylo.common.BaseViewModel
import com.vylo.common.api.apiservis.*
import com.vylo.common.util.SharedPreferenceData
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val commonModule = module {
    single { BaseViewModel(SharedPreferenceData(androidApplication())) }
    single { ApiColumn }
    single { ApiFeed }
    single { ApiUploadVideo }
    single { ApiUploadAudio }
    single { ApiEvents }
    single { ApiUploadThumbnail }
}