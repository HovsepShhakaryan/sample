package com.vylo.main.categorymain.category.domain.usecase.impl
import com.vylo.main.categorymain.category.data.repository.impl.CategoryRepositoryImpl
import com.vylo.main.categorymain.category.domain.usecase.CategoryUseCase

class CategoryUseCaseImpl(
    private val categoryRepository: CategoryRepositoryImpl
) : CategoryUseCase {

    override suspend fun invokeCategories() = categoryRepository.invokeCategories()
}