package com.vylo.signup.signupdonotmissout.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.common.entity.ProfileItem
import com.vylo.signup.signupdonotmissout.domain.entity.response.AutoFollowData

interface DoNotMissOutUseCase {
    suspend fun getAutoFollow(page: Int?): Resource<AutoFollowData>
    suspend fun addPublisher(id: String): Resource<ProfileItem>
    suspend fun deletePublisher(id: String): Resource<ProfileItem>
}