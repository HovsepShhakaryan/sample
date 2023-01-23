package com.vylo.main.globalsearchmain.searchcategories.domain.usecase.impl

import com.vylo.main.globalsearchmain.searchcategories.data.repository.impl.SearchCategoryRepositoryImpl
import com.vylo.main.globalsearchmain.searchcategories.domain.usecase.SearchCategoryUseCase

class SearchCategoryUseCaseImpl(
    private val searchCategoryRepositoryImpl: SearchCategoryRepositoryImpl
) : SearchCategoryUseCase {

    override suspend fun invokeSearchCategory(cursorToken: String?, search: String?) =
        searchCategoryRepositoryImpl.searchCategoryCall(cursorToken, search)

    override suspend fun addCategory(id: Long) =
        searchCategoryRepositoryImpl.addCategory(id)

    override suspend fun deleteCategory(id: Long) =
        searchCategoryRepositoryImpl.deleteCategory(id)

    override suspend fun getProfile() = searchCategoryRepositoryImpl.getProfile()
}