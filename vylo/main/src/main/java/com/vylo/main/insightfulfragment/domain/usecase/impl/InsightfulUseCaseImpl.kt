package com.vylo.main.insightfulfragment.domain.usecase.impl

import com.vylo.main.insightfulfragment.data.repository.InsightfulRepository
import com.vylo.main.insightfulfragment.domain.usecase.InsightfulUseCase

class InsightfulUseCaseImpl(
    private val repository: InsightfulRepository
) : InsightfulUseCase {
    override suspend fun getInsightful(cursor: String?) =
        repository.getInsightful(cursor)
}