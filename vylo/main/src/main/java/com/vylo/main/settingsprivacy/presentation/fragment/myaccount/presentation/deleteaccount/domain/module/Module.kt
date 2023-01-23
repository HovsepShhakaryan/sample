package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.data.repository.DeleteAccountRepository
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.data.repository.impl.DeleteAccountRepositoryImpl
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.domain.usecase.DeleteAccountUseCase
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.domain.usecase.impl.DeleteAccountUseCaseImpl
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.presentation.viewmodel.DeleteAccountViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val deleteAccountModule = module {
    single<DeleteAccountRepository> { DeleteAccountRepositoryImpl(androidApplication()) }
    single<DeleteAccountUseCase> { DeleteAccountUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        DeleteAccountViewModel(
            get(),
            get(),
            androidApplication()
        )
    }
}