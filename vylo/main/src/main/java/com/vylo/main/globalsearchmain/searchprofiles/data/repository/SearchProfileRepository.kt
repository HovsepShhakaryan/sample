package com.vylo.main.globalsearchmain.searchprofiles.data.repository

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileData

interface SearchProfileRepository {
    suspend fun getProfile(): Resource<ProfileItem>
    suspend fun searchProfileCall(
        page: String?,
        search: String?
    ): Resource<ProfileData>

    suspend fun subscribePublisher(id: Long): Resource<ProfileItem>

    suspend fun deleteSubscribedPublisher(id: Long): Resource<ProfileItem>
}