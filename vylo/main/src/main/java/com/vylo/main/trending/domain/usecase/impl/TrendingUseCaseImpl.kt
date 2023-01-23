package com.vylo.main.trending.domain.usecase.impl

import com.vylo.common.util.SharedPreferenceData
import com.vylo.main.trending.data.repository.TrendingRepository
import com.vylo.main.trending.domain.usecase.TrendingUseCase

class TrendingUseCaseImpl(
    private val repository: TrendingRepository,
    private val sharedPreferenceData: SharedPreferenceData
) : TrendingUseCase {
    override suspend fun getTrending(cursor: String?, categoryId: String?) =
        repository.getTrending(cursor, categoryId)

    override suspend fun saveMyGlobalId(globalId: String) {
        sharedPreferenceData.saveUserGlobalId(globalId)
    }

    override suspend fun getMyProfile() = repository.getMyProfile()

    override suspend fun deleteNews(id: String) = repository.deleteNews(id)
}