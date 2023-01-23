package com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.impl

import com.vylo.main.categorymain.categorytrendingfragment.data.repository.CategoryRepository
import com.vylo.main.categorymain.categorytrendingfragment.domain.usecase.CategoryUseCase

class CategoryUseCaseImpl(
    private val repository: CategoryRepository
) : CategoryUseCase {
    override suspend fun getCategory(search: String?, cursor: String?) =
        repository.getCategory(search, cursor)

    override suspend fun getProfile() = repository.getProfile()

    override suspend fun addCategory(id: Long) = repository.addCategory(id)

    override suspend fun deleteCategory(id: Long) = repository.deleteCategory(id)
}