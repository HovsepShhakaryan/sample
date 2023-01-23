package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.usecase.impl

import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.data.repository.UpdatePasswordRepository
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.entity.request.PasswordRequest
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.usecase.UpdatePasswordUseCase

class UpdatePasswordUseCaseImpl(
    private val repository: UpdatePasswordRepository
) : UpdatePasswordUseCase {
    override suspend fun updatePassword(request: PasswordRequest) =
        repository.updatePassword(request)
}