package com.vylo.main.categorymain.category.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.categorymain.category.data.repository.impl.CategoryRepositoryImpl
import com.vylo.main.categorymain.category.domain.usecase.impl.CategoryUseCaseImpl
import com.vylo.main.categorymain.category.presentation.viewmodel.CategoryFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoryModule = module {
    viewModel {
        CategoryFragmentViewModel(
            CategoryUseCaseImpl(CategoryRepositoryImpl(androidContext())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            com.vylo.main.categorymain.category.domain.entity.Mapper(),
            androidApplication()
        )
    }
}