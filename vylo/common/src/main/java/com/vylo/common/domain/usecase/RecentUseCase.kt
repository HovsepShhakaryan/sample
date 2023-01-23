package com.vylo.common.domain.usecase

interface RecentUseCase {
    suspend fun deleteAllRecents()
    suspend fun insertRecent(recent: String)
    suspend fun getRecent(): List<String>?
}