package com.vylo.main.followmain.followingfragment.domain.usecase.impl

import com.vylo.main.followmain.followingfragment.data.repository.FollowingRepository
import com.vylo.main.followmain.followingfragment.domain.usecase.FollowingUseCase

class FollowingUseCaseImpl(
    private val repository: FollowingRepository
) : FollowingUseCase {
    override suspend fun getProfile() = repository.getProfile()

    override suspend fun addPublisher(id: Long) = repository.addPublisher(id)

    override suspend fun deletePublisher(id: Long) = repository.deletePublisher(id)

    override suspend fun addCategory(id: Long) = repository.addCategory(id)

    override suspend fun deleteCategory(id: Long) = repository.deleteCategory(id)

    override suspend fun searchProfileUsersCall(page: String?, search: String?) =
        repository.searchProfileUsersCall(page, search)

    override suspend fun searchProfilePublishersCall(page: String?, search: String?) =
        repository.searchProfilePublishersCall(page, search)
}