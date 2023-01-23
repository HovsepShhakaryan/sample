package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.main.R
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.domain.usecase.DeleteAccountUseCase
import kotlinx.coroutines.launch

class DeleteAccountViewModel(
    private val useCase: DeleteAccountUseCase,
    private val errorUseCase: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application) {
    private val context = application
    val responseSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun deleteAccount() {
        viewModelScope.launch {
            when (val response = useCase.deleteProfile()) {
                is Resource.Success -> {
                    responseSuccess.postValue(context.resources.getString(R.string.delete_account_success))
                }
                is Resource.ApiError -> {
                    errorUseCase.getApiErrorMessage(response.errorData)?.let {
                        responseError.postValue(it.detail)
                    }
                }
                is Resource.Error -> responseError.postValue(context.resources.getString(R.string.delete_account_error))
            }
        }
    }
}