package com.vylo.main.categorymain.categorytrendingfragment.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.PagingUseCaseImpl
import com.vylo.main.categorymain.categorytrendingfragment.data.repository.CategoryRepository
import com.vylo.main.categorymain.categorytrendingfragment.data.repository.impl.CategoryRepositoryImp
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.CategoryUseCase
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.impl.CategoryMapperUseCaseImpl
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.impl.CategoryUseCaseImpl
import com.vylo.main.categorymain.categorytrendingfragment.presentation.viewmodel.CategoryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoryTrendingModule = module {

    single<CategoryRepository> { CategoryRepositoryImp(androidContext()) }
    single<CategoryUseCase> { CategoryUseCaseImpl(get()) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }

    viewModel {
        CategoryViewModel(
            get(),
            get(),
            CategoryMapperUseCaseImpl(com.vylo.main.categorymain.categorytrendingfragment.data.Mapper(Gson())),
            androidApplication()
        )
    }
}