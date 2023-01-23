package com.vylo.main.categorymain.categorytrendingfragment.data.repository

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.main.categorymain.categorytrendingfragment.domain.entity.response.SearchCategoryData

interface CategoryRepository {
    suspend fun getCategory(search: String?, cursor: String?): Resource<SearchCategoryData>
    suspend fun getProfile(): Resource<ProfileItem>
    suspend fun addCategory(id: Long): Resource<ProfileItem>
    suspend fun deleteCategory(id: Long): Resource<ProfileItem>
}