package com.vylo.signup.signupchoosecategory.domain.modul

import com.google.gson.Gson
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.signup.signupchoosecategory.data.repository.impl.CategoryRepositoryImpl
import com.vylo.signup.signupchoosecategory.domain.usecase.CategoryMapperUseCase
import com.vylo.signup.signupchoosecategory.domain.usecase.CategoryUseCase
import com.vylo.signup.signupchoosecategory.domain.usecase.impl.CategoryMapperUseCaseImpl
import com.vylo.signup.signupchoosecategory.domain.usecase.impl.CategoryUseCaseImpl
import com.vylo.signup.signupchoosecategory.presentation.viewmodel.SignUpChooseCategoryFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signUpCategoryModule = module {

    single<CategoryUseCase> { CategoryUseCaseImpl(CategoryRepositoryImpl(androidContext())) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(com.vylo.common.data.Mapper(Gson())) }
    single<CategoryMapperUseCase> { CategoryMapperUseCaseImpl(com.vylo.signup.signupchoosecategory.data.repository.Mapper(Gson())) }

    viewModel {
        SignUpChooseCategoryFragmentViewModel(
            get(),
            get(),
            get(),
            androidApplication()
        )
    }
}