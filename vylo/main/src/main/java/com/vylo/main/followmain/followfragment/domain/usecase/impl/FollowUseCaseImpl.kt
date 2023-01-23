package com.vylo.main.followmain.followfragment.domain.usecase.impl

import com.vylo.main.followmain.followfragment.data.repository.FollowRepository
import com.vylo.main.followmain.followfragment.domain.usecase.FollowUseCase

class FollowUseCaseImpl(
    private val repository: FollowRepository
) : FollowUseCase {
    override suspend fun getFollowers(id: String?, pageSize: Int?, cursor: String?) =
        repository.getFollowers(id, pageSize, cursor)

    override suspend fun getFollowing(id: String?, pageSize: Int?, cursor: String?) =
        repository.getFollowing(id, pageSize, cursor)

    override suspend fun addFollowing(publisherId: Long) =
        repository.addFollowing(publisherId)

    override suspend fun deleteFollowing(publisherId: Long) =
        repository.deleteFollowing(publisherId)
}