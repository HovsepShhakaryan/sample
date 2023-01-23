package com.vylo.main.activity.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.R
import com.vylo.main.activity.domain.entity.NotificationToken
import com.vylo.main.activity.domain.usecase.NotificationTokenUseCase
import com.vylo.main.activity.domain.usecase.impl.NotificationTokenUseCaseImpl
import kotlinx.coroutines.launch

class MainFlowActivityViewModel(
    private val notificationTokenUseCaseImpl: NotificationTokenUseCaseImpl,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun sendNotificationToken(notificationToken: NotificationToken) {
        viewModelScope.launch {
            when (val result = notificationTokenUseCaseImpl.pushNotificationToken(notificationToken)) {
                is Resource.Success -> {
                    if (result.data != null) {
                        responseSuccess.postValue(context.resources.getString(R.string.data_save_successfully))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(result.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(result.errorMessage)
            }
        }
    }
}