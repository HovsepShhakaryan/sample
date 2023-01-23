package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.entity.request.PasswordRequest

interface UpdatePasswordRepository {
    suspend fun updatePassword(request: PasswordRequest): Resource<Any>
}