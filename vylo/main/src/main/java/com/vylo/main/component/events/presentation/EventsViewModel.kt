package com.vylo.main.component.events.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.R
import com.vylo.main.component.events.domain.usecase.EventUseCase
import kotlinx.coroutines.launch

class EventsViewModel(
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    private val eventUseCase: EventUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val eventViewSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val eventViewError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun eventSourceView(ids: List<String>) {
        viewModelScope.launch {
            when (val response = eventUseCase.eventSourceView(ids)) {
                is Resource.Success -> {
                    eventViewSuccess.postValue(context.resources.getString(com.vylo.common.R.string.label_success))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) eventViewError.postValue(apiErrorMessage.detail)
                    else eventViewError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error ->
                    eventViewError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }

    fun eventView(ids: List<String>) {
        viewModelScope.launch {
            when (val response = eventUseCase.eventView(ids)) {
                is Resource.Success -> {
                    eventViewSuccess.postValue(context.resources.getString(com.vylo.common.R.string.label_success))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) eventViewError.postValue(apiErrorMessage.detail)
                    else eventViewError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error ->
                    eventViewError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }

    fun eventVyloView(newsGlobalId: String, timeOfView: String) {
        viewModelScope.launch {
            when (val response = eventUseCase.eventVyloView(newsGlobalId, timeOfView)) {
                is Resource.Success -> {
                    eventViewSuccess.postValue(context.resources.getString(com.vylo.common.R.string.label_success))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) eventViewError.postValue(apiErrorMessage.detail)
                    else eventViewError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error ->
                    eventViewError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }

    fun eventShare(newsGlobalId: String) {
        viewModelScope.launch {
            when (val response = eventUseCase.eventShare(newsGlobalId)) {
                is Resource.Success -> {
                    eventViewSuccess.postValue(context.resources.getString(com.vylo.common.R.string.label_success))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) eventViewError.postValue(apiErrorMessage.detail)
                    else eventViewError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error ->
                    eventViewError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }

    fun eventLike(newsGlobalId: String) {
        viewModelScope.launch {
            when (val response = eventUseCase.eventLike(newsGlobalId)) {
                is Resource.Success -> {
                    eventViewSuccess.postValue(context.resources.getString(com.vylo.common.R.string.label_success))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) eventViewError.postValue(apiErrorMessage.detail)
                    else eventViewError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error ->
                    eventViewError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }
}