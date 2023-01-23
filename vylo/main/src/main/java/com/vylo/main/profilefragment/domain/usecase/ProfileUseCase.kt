package com.vylo.main.profilefragment.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.component.entity.ProfileItem
import com.vylo.main.component.entity.ProfilePhotoItem
import com.vylo.main.profilefragment.domain.entity.request.NewsUpdateRequest
import com.vylo.main.profilefragment.domain.entity.request.ProfileRequest
import com.vylo.main.profilefragment.domain.entity.response.NewsResponse
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsData
import java.io.File

interface ProfileUseCase {
    suspend fun getProfile(): Resource<ProfileItem>
    suspend fun getUserProfile(id: String): Resource<ProfileItem>
    suspend fun updateProfile(data: ProfileRequest): Resource<ProfileItem>
    suspend fun updateProfilePhoto(file: File): Resource<ProfilePhotoItem>
    suspend fun getProfileNews(
        pageSize: Int?,
        cursor: String?
    ): Resource<ProfileNewsData>

    suspend fun getUserProfileNews(
        id: String,
        cursor: String?
    ): Resource<ProfileNewsData>

    suspend fun addPublisher(id: Long): Resource<ProfileItem>
    suspend fun deletePublisher(id: Long): Resource<ProfileItem>
    suspend fun updateNews(id: String, data: NewsUpdateRequest): Resource<NewsResponse>
    suspend fun deleteNews(id: String): Resource<Unit>
}