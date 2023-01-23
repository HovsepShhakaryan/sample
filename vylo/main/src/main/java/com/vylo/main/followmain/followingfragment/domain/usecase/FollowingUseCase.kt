package com.vylo.main.followmain.followingfragment.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileData

interface FollowingUseCase {
    suspend fun getProfile(): Resource<ProfileItem>
    suspend fun addPublisher(id: Long): Resource<ProfileItem>
    suspend fun deletePublisher(id: Long): Resource<ProfileItem>
    suspend fun addCategory(id: Long): Resource<ProfileItem>
    suspend fun deleteCategory(id: Long): Resource<ProfileItem>

    suspend fun searchProfileUsersCall(
        page: String?,
        search: String?
    ): Resource<ProfileData>

    suspend fun searchProfilePublishersCall(
        page: String?,
        search: String?
    ): Resource<ProfileData>
}