package com.vylo.signup.signupdonotmissout.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.entity.PublishersSubscription
import com.vylo.signup.R
import com.vylo.signup.signupdonotmissout.domain.entity.response.AutoFollowItem
import com.vylo.signup.signupdonotmissout.domain.usecase.DoNotMissOutMapperUseCase
import com.vylo.signup.signupdonotmissout.domain.usecase.DoNotMissOutUseCase
import kotlinx.coroutines.launch

class SignUpDoNotMissOutFragmentViewModel(
    private val useCase: DoNotMissOutUseCase,
    private val mapperUseCase: DoNotMissOutMapperUseCase,
    private val errorUseCase: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val autoFollowSuccess: SingleLiveEvent<List<AutoFollowItem>> by lazy { SingleLiveEvent() }
    val autoFollowError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val publishersSuccess: SingleLiveEvent<List<PublishersSubscription>> by lazy { SingleLiveEvent() }

    fun getAutoFollow(page: Int? = null) {
        viewModelScope.launch {
            when (val response = useCase.getAutoFollow(page)) {
                is Resource.Success -> {
                    response.data?.let {
                        val list = mapperUseCase.fromAutoFollowDataToAutoFollowList(it)
                        autoFollowSuccess.postValue(list)
                    } ?: run {
                        autoFollowError.postValue(context.resources.getString(R.string.label_no_data_available))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) autoFollowError.postValue(errorMessage.detail)
                    else autoFollowError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> autoFollowError.postValue(response.errorMessage)
            }
        }
    }

    fun follow(id: String) {
        viewModelScope.launch {
            when (val response = useCase.addPublisher(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        publishersSuccess.postValue(it.publishersSubscription)
                    } ?: run {
                        autoFollowError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    errorUseCase.getApiErrorMessage(response.errorData)?.let {
                        autoFollowError.postValue(it.detail)
                    }
                }
                is Resource.Error -> autoFollowError.postValue(response.errorMessage)
            }
        }
    }

    fun unfollow(id: String) {
        viewModelScope.launch {
            when (val response = useCase.deletePublisher(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        publishersSuccess.postValue(it.publishersSubscription)
                    } ?: run {
                        autoFollowError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    errorUseCase.getApiErrorMessage(response.errorData)?.let {
                        autoFollowError.postValue(it.detail)
                    }
                }
                is Resource.Error -> autoFollowError.postValue(response.errorMessage)
            }
        }
    }
}