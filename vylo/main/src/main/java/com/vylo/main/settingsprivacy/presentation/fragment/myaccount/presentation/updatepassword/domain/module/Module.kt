package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.data.repository.UpdatePasswordRepository
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.data.repository.impl.UpdatePasswordRepositoryImpl
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.usecase.UpdatePasswordUseCase
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.usecase.impl.UpdatePasswordUseCaseImpl
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.presentation.viewmodel.UpdatePasswordViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val updatePasswordModule = module {
    single<UpdatePasswordRepository> { UpdatePasswordRepositoryImpl(androidApplication()) }
    single<UpdatePasswordUseCase> { UpdatePasswordUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        UpdatePasswordViewModel(
            get(),
            get(),
            androidApplication()
        )
    }
}