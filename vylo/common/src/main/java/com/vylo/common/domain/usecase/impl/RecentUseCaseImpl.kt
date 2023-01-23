package com.vylo.common.domain.usecase.impl

import com.vylo.common.data.Mapper
import com.vylo.common.domain.localentity.entity.RecentTypes
import com.vylo.common.data.repository.impl.RecentRepositoryImpl
import com.vylo.common.domain.usecase.RecentUseCase

class RecentUseCaseImpl(
    private val recentTrendingRepositoryImpl: RecentRepositoryImpl,
    private val mapper: Mapper
) : RecentUseCase {

    override suspend fun deleteAllRecents() {
        recentTrendingRepositoryImpl.deleteAllRecents()
    }

    override suspend fun insertRecent(recent: String) {
        val recents = getRecent()
        val isExistText = recents.any { it == recent }
        if (recents.isNotEmpty()) {
            if (!isExistText) recentTrendingRepositoryImpl.insertRecents(RecentTypes(recent))
        } else recentTrendingRepositoryImpl.insertRecents(RecentTypes(recent))
    }

    override suspend fun getRecent() =
        mapper.fromRecentTypeToList(recentTrendingRepositoryImpl.getRecents())

}