package com.vylo.main.following.data.repository

import com.vylo.common.api.Resource
import com.vylo.common.domain.localentity.entity.PublishersEntity
import com.vylo.common.entity.PublishersSubscription

interface PublisherRepository {
    suspend fun getPublishers(): Resource<List<PublishersSubscription>?>
    suspend fun isFollowing(globalId: String): Resource<Boolean>
    suspend fun save(item: PublishersEntity)
    suspend fun saveAll(items: List<PublishersEntity>)
    suspend fun delete(id: Long)
    suspend fun deleteAll()
}