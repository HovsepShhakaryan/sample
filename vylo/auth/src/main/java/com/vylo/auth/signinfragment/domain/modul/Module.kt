package com.vylo.auth.signinfragment.domain.modul

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.auth.signinfragment.data.repository.impl.SignInRepositoryImpl
import com.vylo.auth.signinfragment.domain.usecase.impl.AuthValidationUseCaseImpl
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.auth.signinfragment.domain.usecase.impl.SignInUseCaseImpl
import com.vylo.auth.signinfragment.presentation.viewmodel.SignInFragmentViewModel
import com.vylo.common.domain.usecase.impl.ValidationUseCaseImpl
import com.vylo.common.util.SharedPreferenceData
import com.vylo.common.validation.Validation
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel {
        SignInFragmentViewModel(
            SignInUseCaseImpl(SignInRepositoryImpl(androidContext()), SharedPreferenceData(androidContext())),
            AuthValidationUseCaseImpl(ValidationUseCaseImpl(Validation(androidContext()))),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            androidApplication()
        )
    }
}