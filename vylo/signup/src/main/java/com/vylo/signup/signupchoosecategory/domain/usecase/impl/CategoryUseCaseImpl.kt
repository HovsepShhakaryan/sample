package com.vylo.signup.signupchoosecategory.domain.usecase.impl

import com.vylo.signup.signupchoosecategory.data.repository.impl.CategoryRepositoryImpl
import com.vylo.signup.signupchoosecategory.domain.usecase.CategoryUseCase

class CategoryUseCaseImpl(
    private val categoryRepositoryImpl: CategoryRepositoryImpl
) : CategoryUseCase {

    override suspend fun invokeSearchCategory(cursorToken: String?, search: String?) =
        categoryRepositoryImpl.searchCategoryCall(cursorToken, search)

    override suspend fun addCategory(id: Long) =
        categoryRepositoryImpl.addCategory(id)

    override suspend fun deleteCategory(id: Long) =
        categoryRepositoryImpl.deleteCategory(id)
}