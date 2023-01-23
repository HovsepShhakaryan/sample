package com.vylo.main.globalsearchmain.searchmain.domain.module

import com.google.gson.Gson
import com.vylo.common.data.Mapper
import com.vylo.common.data.repository.impl.RecentRepositoryImpl
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.domain.usecase.impl.RecentUseCaseImpl
import com.vylo.main.globalsearchmain.searchcategories.data.repository.impl.SearchCategoryRepositoryImpl
import com.vylo.globalsearch.searchcategories.domain.usecase.impl.SearchCategoryMapperUseCaseImpl
import com.vylo.main.globalsearchmain.searchcategories.domain.usecase.impl.SearchCategoryUseCaseImpl
import com.vylo.main.globalsearchmain.searchcategories.presentation.viewmodel.SearchCategoriesFragmentViewModel
import com.vylo.main.globalsearchmain.searchmain.presentation.viewmodel.SearchMainFragmentViewModel
import com.vylo.globalsearch.searchnewsstand.data.repository.impl.NewsstandRepositoryImpl
import com.vylo.globalsearch.searchnewsstand.domain.usecase.impl.SearchNewsstandMapperUseCaseImpl
import com.vylo.globalsearch.searchnewsstand.domain.usecase.impl.SearchNewsstandUseCaseImpl
import com.vylo.globalsearch.searchprofiles.domain.usecase.impl.SearchProfileMapperUseCaseImpl
import com.vylo.main.globalsearchmain.searchnewsstand.presentation.viewmodel.SearchNewsstandFragmentViewModel
import com.vylo.main.globalsearchmain.searchprofiles.data.repository.impl.SearchProfileRepositoryImpl
import com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.SearchProfileMapperUseCase
import com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.SearchProfileUseCase
import com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.impl.SearchProfileUseCaseImpl
import com.vylo.main.globalsearchmain.searchprofiles.presentation.viewmodel.SearchProfileFragmentViewModel
import com.vylo.globalsearch.searchvylo.data.repository.impl.SearchVyloRepositoryImpl
import com.vylo.main.globalsearchmain.searchvylo.domain.usecase.impl.SearchVyloMapperUseCaseImpl
import com.vylo.globalsearch.searchvylo.domain.usecase.impl.SearchVyloUseCaseImpl
import com.vylo.main.globalsearchmain.searchvylo.presentation.viewmodel.SearchVyloFragmentViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchMainModule = module {
    viewModel {
        SearchMainFragmentViewModel()
    }

    viewModel {
        SearchVyloFragmentViewModel(
            SearchVyloUseCaseImpl(SearchVyloRepositoryImpl(androidContext())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            SearchVyloMapperUseCaseImpl(com.vylo.globalsearch.searchvylo.data.repository.Mapper(Gson())),
            RecentUseCaseImpl(
                RecentRepositoryImpl(androidContext()),
                Mapper(Gson())
            ),
            androidApplication()
        )
    }

    viewModel {
        SearchNewsstandFragmentViewModel(
            SearchNewsstandUseCaseImpl(NewsstandRepositoryImpl(androidContext())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            SearchNewsstandMapperUseCaseImpl(com.vylo.globalsearch.searchnewsstand.data.repository.Mapper(Gson())),
            RecentUseCaseImpl(
                RecentRepositoryImpl(androidContext()),
                Mapper(Gson())
            ),
            androidApplication()
        )
    }

    viewModel {
        SearchCategoriesFragmentViewModel(
            SearchCategoryUseCaseImpl(SearchCategoryRepositoryImpl(androidContext())),
            GeneralErrorMapperUseCaseImpl(Mapper(Gson())),
            SearchCategoryMapperUseCaseImpl(com.vylo.globalsearch.searchcategories.data.repository.Mapper(Gson())),
            RecentUseCaseImpl(
                RecentRepositoryImpl(androidContext()),
                Mapper(Gson())
            ),
            androidApplication()
        )
    }

    single<SearchProfileUseCase> { SearchProfileUseCaseImpl(SearchProfileRepositoryImpl(androidContext())) }
    single<GeneralErrorMapperUseCase> { GeneralErrorMapperUseCaseImpl(Mapper(Gson())) }
    single<SearchProfileMapperUseCase> { SearchProfileMapperUseCaseImpl(com.vylo.main.globalsearchmain.searchprofiles.data.repository.Mapper(Gson())) }

    viewModel {
        SearchProfileFragmentViewModel(
            get(),
            get(),
            get(),
            RecentUseCaseImpl(
                RecentRepositoryImpl(androidContext()),
                Mapper(Gson())
            ),
            androidApplication()
        )
    }
}