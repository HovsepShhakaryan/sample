package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.domain.usecase

import com.vylo.common.api.Resource

interface DeleteAccountUseCase {
    suspend fun deleteProfile(): Resource<Unit>
}