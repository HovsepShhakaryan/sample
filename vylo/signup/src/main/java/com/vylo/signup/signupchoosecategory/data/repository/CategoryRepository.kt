package com.vylo.signup.signupchoosecategory.data.repository

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryData

interface CategoryRepository {

    suspend fun searchCategoryCall(
        cursorToken: String?,
        search: String?
    ): Resource<CategoryData>

    suspend fun addCategory(id: Long): Resource<ProfileItem>

    suspend fun deleteCategory(id: Long): Resource<ProfileItem>
}