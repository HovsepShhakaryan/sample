package com.vylo.androidapplication.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.androidapplication.domain.usecase.impl.VersionUseCaseImpl
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.R
import kotlinx.coroutines.launch

class StartActivityViewModel(
    private val versionUseCase: VersionUseCaseImpl,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun getAppVersionName() {
        viewModelScope.launch {
            when (val result = versionUseCase.getAppVersionName()) {
                is Resource.Success -> {
                    if (result.data != null) {
                        if (result.data!!.android != null){
                            responseSuccess.postValue(result.data!!.android!!.appVersion)
                        } else responseSuccess.postValue(null)
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