package com.vylo.common.domain.usecase.impl

import com.vylo.common.domain.usecase.MyProfileUseCase
import com.vylo.common.util.SharedPreferenceData

class MyProfileUseCaseImpl(
    private val sharedPreferenceData: SharedPreferenceData
) : MyProfileUseCase {
    override val myGlobalId: String?
        get() = sharedPreferenceData.myGlobalId

    override suspend fun saveMyGlobalId(globalId: String) {
        sharedPreferenceData.saveUserGlobalId(globalId)
    }

    override suspend fun removeMyGlobalId() {
        sharedPreferenceData.removeUserGlobalId()
    }
}