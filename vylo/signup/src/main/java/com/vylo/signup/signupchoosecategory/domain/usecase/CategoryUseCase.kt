package com.vylo.signup.signupchoosecategory.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryData

interface CategoryUseCase {
    suspend fun invokeSearchCategory(
        cursorToken: String?,
        search: String?
    ): Resource<CategoryData>

    suspend fun addCategory(id: Long): Resource<ProfileItem>

    suspend fun deleteCategory(id: Long): Resource<ProfileItem>
}