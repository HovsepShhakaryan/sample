package com.vylo.globalsearch.searchcategories.data.repository

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.SearchCategoryData

interface SearchCategoryRepository {

    suspend fun searchCategoryCall(
        cursorToken: String?,
        search: String?
    ): Resource<SearchCategoryData>

    suspend fun addCategory(id: Long): Resource<ProfileItem>

    suspend fun deleteCategory(id: Long): Resource<ProfileItem>

    suspend fun getProfile(): Resource<ProfileItem>
}