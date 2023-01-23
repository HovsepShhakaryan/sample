package com.vylo.main.following.data.repository.impl

import android.content.Context
import com.vylo.common.api.Resource
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.common.db.RecentDatabase
import com.vylo.common.domain.localentity.entity.PublishersEntity
import com.vylo.common.domain.localentity.entity.toPublishersSubscription
import com.vylo.common.entity.PublishersSubscription
import com.vylo.main.following.data.endpoint.PublishersApi
import com.vylo.main.following.data.repository.PublisherRepository
import org.koin.java.KoinJavaComponent

class PublisherRepositoryImpl(
    context: Context
) : BaseRepo(), PublisherRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(PublishersApi::class.java)

    private val dao = RecentDatabase.getDatabase(context).publisherDao()

    override suspend fun getPublishers(): Resource<List<PublishersSubscription>?> {
        val publishers = dao.getAllPublishers()
        return if (publishers.isNullOrEmpty()) {
            safeApiCall { client.getProfile() }.let {
                when (it) {
                    is Resource.Success -> Resource.Success(data = it.data?.publishersSubscription)
                    is Resource.ApiError -> Resource.ApiError(error = it.errorData)
                    is Resource.Error -> Resource.Error(errorMessage = it.errorMessage.orEmpty())
                }
            }
        } else {
            Resource.Success(data = publishers.map { it.toPublishersSubscription() })
        }
    }

    override suspend fun isFollowing(globalId: String): Resource<Boolean> {
//        val publishers = dao.getAllPublishers()
//        return if (publishers.isNullOrEmpty()) {
            return safeApiCall { client.getProfile() }.let {
                when (it) {
                    is Resource.Success -> Resource.Success(data = it.data?.publishersSubscription?.find { it.globalId == globalId } != null)
                    is Resource.ApiError -> Resource.ApiError(error = it.errorData)
                    is Resource.Error -> Resource.Error(errorMessage = it.errorMessage.orEmpty())
                }
            }
//        } else {
//            Resource.Success(data = publishers.map { it.toPublishersSubscription() }
//                .find { it.globalId == globalId } != null)
//        }

    }

    override suspend fun save(item: PublishersEntity) {
        dao.save(item)
    }

    override suspend fun saveAll(items: List<PublishersEntity>) {
        dao.saveAll(items)
    }

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}