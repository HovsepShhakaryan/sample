package com.vylo.signup.signupdonotmissout.domain.usecase

import com.vylo.signup.signupdonotmissout.domain.entity.response.AutoFollowData
import com.vylo.signup.signupdonotmissout.domain.entity.response.AutoFollowItem

interface DoNotMissOutMapperUseCase {
    fun fromAutoFollowDataToAutoFollowList(data: AutoFollowData): List<AutoFollowItem>?
    fun getPagingToken(): String?
}