package com.vylo.main.profilefragment.domain.usecase.impl

import com.vylo.main.profilefragment.data.repository.ProfileRepository
import com.vylo.main.profilefragment.domain.entity.request.NewsUpdateRequest
import com.vylo.main.profilefragment.domain.entity.request.ProfileRequest
import com.vylo.main.profilefragment.domain.usecase.ProfileUseCase
import java.io.File

class ProfileUseCaseImpl(
    private val repository: ProfileRepository
) : ProfileUseCase {
    override suspend fun getProfile() = repository.getProfile()
    override suspend fun getUserProfile(id: String) = repository.getUserProfile(id)
    override suspend fun updateProfile(data: ProfileRequest) = repository.updateProfile(data)
    override suspend fun updateProfilePhoto(file: File) = repository.updateProfilePhoto(file)

    override suspend fun getProfileNews(pageSize: Int?, cursor: String?) =
        repository.getProfileNews(pageSize, cursor)

    override suspend fun getUserProfileNews(
        id: String,
        cursor: String?
    ) = repository.getUserProfileNews(id, cursor)

    override suspend fun addPublisher(id: Long) = repository.addPublisher(id)

    override suspend fun deletePublisher(id: Long) = repository.deletePublisher(id)
    override suspend fun updateNews(id: String, data: NewsUpdateRequest) =
        repository.updateNews(id, data)

    override suspend fun deleteNews(id: String) = repository.deleteNews(id)
}