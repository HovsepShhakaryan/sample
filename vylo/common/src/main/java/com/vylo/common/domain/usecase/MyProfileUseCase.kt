package com.vylo.common.domain.usecase

interface MyProfileUseCase {
    val myGlobalId: String?
    suspend fun saveMyGlobalId(globalId: String)
    suspend fun removeMyGlobalId()
}