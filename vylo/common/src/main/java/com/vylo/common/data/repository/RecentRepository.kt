package com.vylo.common.data.repository

import com.vylo.common.domain.localentity.entity.RecentTypes

interface RecentRepository {
    suspend fun deleteAllRecents()
    suspend fun insertRecents(recent: RecentTypes)
    suspend fun getRecents(): List<RecentTypes>?
}