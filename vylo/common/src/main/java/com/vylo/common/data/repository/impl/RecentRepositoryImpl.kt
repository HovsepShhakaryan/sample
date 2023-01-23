package com.vylo.common.data.repository.impl

import android.content.Context
import com.vylo.common.db.RecentDatabase
import com.vylo.common.domain.localentity.entity.RecentTypes
import com.vylo.common.data.repository.RecentRepository

class RecentRepositoryImpl(
    context: Context
) : RecentRepository {

    private val recentDao = RecentDatabase.getDatabase(context).recentDao()

    override suspend fun getRecents() = recentDao.getRecentTypes()
    override suspend fun deleteAllRecents() = recentDao.deleteAll()
    override suspend fun insertRecents(recent: RecentTypes) {
        recentDao.insert(recent)
    }


}