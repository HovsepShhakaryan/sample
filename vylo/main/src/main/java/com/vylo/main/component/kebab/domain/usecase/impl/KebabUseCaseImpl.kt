package com.vylo.main.component.kebab.domain.usecase.impl

import com.vylo.common.api.Resource
import com.vylo.main.component.kebab.data.repository.KebabRepository
import com.vylo.main.component.kebab.domain.entity.request.BlockUser
import com.vylo.main.component.kebab.domain.entity.request.ReportRequest
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase

class KebabUseCaseImpl(
    private val repository: KebabRepository
) : KebabUseCase {
    override suspend fun getEventCounter(id: String) = repository.getEventCounter(id)

    override suspend fun addLike(id: String) = repository.addLike(id)

    override suspend fun deleteLike(id: String) = repository.deleteLike(id)

    override suspend fun sendReport(report: ReportRequest) = repository.sendReport(report)

    override suspend fun blockUser(blockUser: BlockUser) = repository.blockUser(blockUser)

    override suspend fun shareReport(id: String) = repository.shareReport(id)

    override suspend fun viewReport(id: String) = repository.viewReport(id)
}