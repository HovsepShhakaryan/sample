package com.vylo.main.homefragment.domain.usecase.impl

import com.vylo.common.util.SharedPreferenceData
import com.vylo.main.homefragment.data.repository.impl.FeedRepositoryImpl
import com.vylo.main.homefragment.domain.usecase.FeedUseCase

class FeedUseCaseImpl(
    private val feedRepositoryImpl: FeedRepositoryImpl,
    private val sharedPreferenceData: SharedPreferenceData
) : FeedUseCase {

    override suspend fun invokeFeed(cursorToken: String?) =
        feedRepositoryImpl.getFeedCall(cursorToken)

    override suspend fun saveMyGlobalId(globalId: String) {
        sharedPreferenceData.saveUserGlobalId(globalId)
    }

    override suspend fun getMyProfile() = feedRepositoryImpl.getMyProfile()
    override suspend fun deleteNews(id: String) = feedRepositoryImpl.deleteNews(id)
}