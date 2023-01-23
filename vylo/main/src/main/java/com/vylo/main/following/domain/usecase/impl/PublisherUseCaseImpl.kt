package com.vylo.main.following.domain.usecase.impl

import com.vylo.common.domain.localentity.entity.PublishersEntity
import com.vylo.main.following.data.repository.PublisherRepository
import com.vylo.main.following.domain.usecase.PublisherUseCase

class PublisherUseCaseImpl(
    private val repository: PublisherRepository
) : PublisherUseCase {

    override suspend fun getPublishers() = repository.getPublishers()

    override suspend fun isFollowing(globalId: String) = repository.isFollowing(globalId)

    override suspend fun save(item: PublishersEntity) {
        repository.save(item)
    }

    override suspend fun saveAll(items: List<PublishersEntity>) {
        repository.saveAll(items)
    }

    override suspend fun delete(id: Long) {
        repository.delete(id)
    }

    override suspend fun deleteAll() {
        repository.deleteAll()
    }
}