package com.vylo.main.component.kebab.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.component.kebab.domain.entity.request.BlockUser
import com.vylo.main.component.kebab.domain.entity.request.ReportRequest
import com.vylo.main.component.kebab.domain.entity.response.EventCounterItem

interface KebabRepository {
    suspend fun getEventCounter(id: String): Resource<EventCounterItem>
    suspend fun addLike(id: String): Resource<Unit>
    suspend fun deleteLike(id: String): Resource<Unit>
    suspend fun sendReport(report: ReportRequest): Resource<Any>
    suspend fun blockUser(blockUser: BlockUser): Resource<Any>
    suspend fun shareReport(id: String): Resource<Unit>
    suspend fun viewReport(id: String): Resource<Unit>
}