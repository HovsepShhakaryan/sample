package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.domain.usecase.impl

import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.data.repository.DeleteAccountRepository
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.domain.usecase.DeleteAccountUseCase

class DeleteAccountUseCaseImpl(
    private val repository: DeleteAccountRepository
) : DeleteAccountUseCase {
    override suspend fun deleteProfile() = repository.deleteProfile()
}