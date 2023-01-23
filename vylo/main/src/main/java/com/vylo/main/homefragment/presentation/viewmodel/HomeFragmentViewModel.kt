package com.vylo.main.homefragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.MyProfileUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.common.ext.toText
import com.vylo.main.R
import com.vylo.main.component.kebab.domain.entity.request.ReportRequest
import com.vylo.main.component.kebab.domain.entity.response.EventCounterData
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.homefragment.domain.usecase.impl.FeedMapperUseCaseImpl
import com.vylo.main.homefragment.domain.usecase.impl.FeedUseCaseImpl
import kotlinx.coroutines.launch

class HomeFragmentViewModel(
    private val feedUseCaseImpl: FeedUseCaseImpl,
    private val kebabUseCase: KebabUseCase,
    private val myProfileUseCase: MyProfileUseCase,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    private val feedMapperUseCaseImpl: FeedMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    private var actualData: List<FeedItem>? = null
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<List<FeedItem>> by lazy { SingleLiveEvent() }
    val deleteResponseSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val myGlobalId: String?
        get() = myProfileUseCase.myGlobalId

    fun setActualData(actualData: List<FeedItem>) {
        this.actualData = actualData
    }

    fun getActualData() = this.actualData

    val eventError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val eventSuccess: SingleLiveEvent<EventCounterData> by lazy { SingleLiveEvent() }

    fun getFeed() {
        viewModelScope.launch {
            if (!feedMapperUseCaseImpl.isBrakeCall())
                when (val feedResponse =
                    feedUseCaseImpl.invokeFeed(feedMapperUseCaseImpl.getPagingToken())) {
                    is Resource.Success -> {
                        if (feedResponse.data != null) {
                            val feedData =
                                feedMapperUseCaseImpl.fromFeedDataToFeedItemList(feedResponse.data!!)
                            responseSuccess.postValue(feedData)
                        } else responseError.postValue("")
                    }
                    is Resource.ApiError -> {
                        val apiErrorMessage =
                            generalErrorMapperUseCaseImpl.getApiErrorMessage(feedResponse.errorData)
                        if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                        else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                    }
                    is Resource.Error -> responseError.postValue(feedResponse.errorMessage)
                }
            else responseError.postValue(context.resources.getString(R.string.label_no_feed))
        }

    }

    fun getMyProfile() {
        viewModelScope.launch {
            when (val signInResponse = feedUseCaseImpl.getMyProfile()) {
                is Resource.Success -> {
                    signInResponse.data?.globalId?.let { id ->
                        feedUseCaseImpl.saveMyGlobalId(id)
                    } ?: run {
                        responseError.postValue(context.resources.getString(R.string.no_profile_data))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(signInResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(signInResponse.errorMessage)
            }
        }
    }

    fun setBrakeCall(isBrakeCall: Boolean) {
        feedMapperUseCaseImpl.setBrakeCall(isBrakeCall)
    }

    fun getEventCounter(id: String) {
        viewModelScope.launch {
            when (val response = kebabUseCase.getEventCounter(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        eventSuccess.postValue(
                            EventCounterData(
                                globalId = id,
                                eventCounterItem = it
                            )
                        )
                    } ?: run {
                        eventError.postValue(context.resources.getString(R.string.no_news_data))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) eventError.postValue(apiErrorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun addLike(data: EventCounterData) {
        viewModelScope.launch {
            when (val response = kebabUseCase.addLike(data.globalId)) {
                is Resource.Success -> {
                    getEventCounter(data.globalId)
                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) eventError.postValue(apiErrorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun deleteLike(data: EventCounterData) {
        viewModelScope.launch {
            when (val response = kebabUseCase.deleteLike(data.globalId)) {
                is Resource.Success -> {
                    getEventCounter(data.globalId)
                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) eventError.postValue(apiErrorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
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
                    if (apiErrorMessage?.detail != null) eventError.postValue(apiErrorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
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
                    if (apiErrorMessage?.detail != null) eventError.postValue(apiErrorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun deleteNews(id: String) {
        viewModelScope.launch {
            when (val response = feedUseCaseImpl.deleteNews(id)) {
                is Resource.Success -> {
                    deleteResponseSuccess.postValue(id)
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiUpdateNewsErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responseError.postValue(errorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(response.errorMessage)
            }
        }
    }

}