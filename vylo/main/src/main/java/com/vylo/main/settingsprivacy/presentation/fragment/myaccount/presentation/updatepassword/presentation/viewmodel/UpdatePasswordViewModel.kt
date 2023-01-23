package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.ext.toText
import com.vylo.main.R
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.entity.request.PasswordRequest
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.usecase.UpdatePasswordUseCase
import kotlinx.coroutines.launch

class UpdatePasswordViewModel(
    private val useCase: UpdatePasswordUseCase,
    private val errorUseCase: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun updatePassword(request: PasswordRequest) {
        viewModelScope.launch {
            when (val response = useCase.updatePassword(request)) {
                is Resource.Success -> {
                    responseSuccess.postValue(context.resources.getString(R.string.success_password_changed))
                }
                is Resource.ApiError -> {
                    errorUseCase.getApiPasswordErrorMessage(response.errorData)?.let {
                        val error = it.currentPassword ?: it.newPassword ?: it.reNewPassword
                        ?: it.nonFieldPassword
                        error?.let {
                            responseError.postValue(it.toText())
                        } ?: run {
                            responseError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                        }
                    } ?: run {
                        responseError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                    }
                }
                is Resource.Error -> responseError.postValue(response.errorMessage)
            }
        }
    }
}