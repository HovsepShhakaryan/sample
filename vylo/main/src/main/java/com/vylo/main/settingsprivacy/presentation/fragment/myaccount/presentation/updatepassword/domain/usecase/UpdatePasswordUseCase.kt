package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.entity.request.PasswordRequest

interface UpdatePasswordUseCase {
    suspend fun updatePassword(request: PasswordRequest): Resource<Any>
}