package com.vylo.signup.signupcreateusername.data.repository

import com.vylo.common.api.Resource
import com.vylo.signup.signupcreateusername.domain.entity.request.DraftUserName
import com.vylo.signup.signupcreateusername.domain.entity.response.DraftUserNameData

interface SignUpUsernameRepository {

    suspend fun checkUsername(
        draftUserName: DraftUserName
    ): Resource<DraftUserNameData>
}