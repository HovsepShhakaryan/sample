package com.vylo.main.webfragment.presentation.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.ext.toText
import com.vylo.main.R
import com.vylo.main.component.kebab.domain.entity.request.ReportRequest
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.videofragment.domain.usecase.VideoUseCase
import kotlinx.coroutines.launch

class WebViewModel(
    private val useCase: VideoUseCase,
    private val kebabUseCase: KebabUseCase,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val newsSuccess: SingleLiveEvent<FeedItem> by lazy { SingleLiveEvent() }
    val newsError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun getFeedById(id: String) {
        viewModelScope.launch {
            when (val response = useCase.getFeedById(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        newsSuccess.postValue(it)
                    } ?: run {
                        newsError.postValue(context.resources.getString(R.string.no_news_data))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) newsError.postValue(errorMessage.detail)
                    else newsError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> newsError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }

    fun respond() {
        Toast.makeText(context, "respond()", Toast.LENGTH_SHORT).show()
    }

    fun responses() {
        Toast.makeText(context, "responses()", Toast.LENGTH_SHORT).show()
    }

    fun sendReport(status: Int, globalId: String) {
        viewModelScope.launch {
            val request = ReportRequest(status, globalId)
            when (val response = kebabUseCase.sendReport(request)) {
                is Resource.Success -> {

                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        generalErrorMapperUseCaseImpl.getApiReportErrorMessage(response.errorData)
                    if (apiErrorMessage?.newsGlobalId != null) responseError.postValue(
                        apiErrorMessage.newsGlobalId!!.toText()
                    )
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    responseError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun shareReport(id: String) {
        viewModelScope.launch {
            when (val response = kebabUseCase.shareReport(id)) {
                is Resource.Success -> {
//                    getEventCounter(data.globalId)
                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    responseError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun viewReport(id: String) {
        viewModelScope.launch {
            when (val response = kebabUseCase.viewReport(id)) {
                is Resource.Success -> {
//                    getEventCounter(data.globalId)
                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    responseError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

}