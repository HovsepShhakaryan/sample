package com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.impl

import com.vylo.main.globalsearchmain.searchprofiles.data.repository.impl.SearchProfileRepositoryImpl
import com.vylo.main.globalsearchmain.searchprofiles.domain.usecase.SearchProfileUseCase

class SearchProfileUseCaseImpl(
    private val searchProfileRepositoryImpl: SearchProfileRepositoryImpl
) : SearchProfileUseCase {

    override suspend fun getProfile() = searchProfileRepositoryImpl.getProfile()

    override suspend fun searchProfileCall(page: String?, search: String?) =
        searchProfileRepositoryImpl.searchProfileCall(page, search)

    override suspend fun subscribePublisher(id: Long) =
        searchProfileRepositoryImpl.subscribePublisher(id)

    override suspend fun deleteSubscribedPublisher(id: Long) =
        searchProfileRepositoryImpl.deleteSubscribedPublisher(id)
}