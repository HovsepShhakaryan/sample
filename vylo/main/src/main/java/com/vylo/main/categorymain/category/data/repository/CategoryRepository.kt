package com.vylo.main.categorymain.category.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.categorymain.category.domain.entity.Categories

interface CategoryRepository {
    suspend fun invokeCategories(): Resource<Categories>
}