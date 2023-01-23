package com.vylo.main.categorymain.category.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.categorymain.category.domain.entity.Categories

interface CategoryUseCase {
    suspend fun invokeCategories(): Resource<Categories>
}