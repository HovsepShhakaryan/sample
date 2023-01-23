package com.vylo.main.videofragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.MyProfileUseCase
import com.vylo.common.ext.orZero
import com.vylo.common.ext.toText
import com.vylo.main.R
import com.vylo.main.component.kebab.domain.entity.request.ReportRequest
import com.vylo.main.component.kebab.domain.entity.response.EventCounterData
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.following.domain.usecase.PublisherUseCase
import com.vylo.main.followmain.followfragment.domain.usecase.FollowUseCase
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.profilefragment.domain.usecase.ProfileUseCase
import com.vylo.main.videofragment.domain.usecase.VideoUseCase
import com.vylo.main.videofragment.domain.usecase.impl.VideoMapperUseCaseImpl
import kotlinx.coroutines.launch

class VideoViewModel(
    private val kebabUseCase: KebabUseCase,
    private val videoUseCase: VideoUseCase,
    private val myProfileUseCase: MyProfileUseCase,
    private val publisherUseCase: PublisherUseCase,
    private val profileUseCase: ProfileUseCase,
    private val useCase: FollowUseCase,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCase,
    private val videoMapperUseCaseImpl: VideoMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val videoListSuccess: SingleLiveEvent<List<FeedItem>> by lazy { SingleLiveEvent() }

    val eventError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val eventSuccess: SingleLiveEvent<EventCounterData> by lazy { SingleLiveEvent() }

    val videoSuccess: SingleLiveEvent<FeedItem> by lazy { SingleLiveEvent() }
    val videoError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val deleteResponseSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val isFollowing: SingleLiveEvent<Map<String, Boolean>> by lazy { SingleLiveEvent() }
    val publisherId: SingleLiveEvent<Map<String, Long>> by lazy { SingleLiveEvent() }

    val myGlobalId: String?
        get() = myProfileUseCase.myGlobalId

    fun getResponses(id: String) {
        viewModelScope.launch {
            if (!videoMapperUseCaseImpl.isBrakeCall()) {
                when (val response =
                    videoUseCase.getResponses(id, videoMapperUseCaseImpl.getPagingToken())) {
                    is Resource.Success -> {
                        response.data?.let {
                            val videoData =
                                videoMapperUseCaseImpl.fromFeedDataToFeedItemList(response.data!!)
                            videoListSuccess.postValue(videoData)
                        } ?: run {
                            videoError.postValue(context.resources.getString(R.string.no_news_data))
                        }
                    }
                    is Resource.ApiError -> {
                        val errorMessage =
                            generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                        if (errorMessage?.detail != null) videoError.postValue(errorMessage.detail)
                        else videoError.postValue(context.resources.getString(R.string.label_something_wrong))
                    }
                    is Resource.Error -> {
                        videoError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                    }
                }
            }
        }
    }

    fun getFeedById(id: String) {
        viewModelScope.launch {
            when (val response = videoUseCase.getFeedById(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        videoSuccess.postValue(it)
                        getEventCounter(id)
                    } ?: run {
                        videoError.postValue(context.resources.getString(R.string.no_news_data))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) videoError.postValue(errorMessage.detail)
                    else videoError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> videoError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }

    fun getMyFeedById(id: String) {
        viewModelScope.launch {
            when (val response = videoUseCase.getMyFeedById(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        videoSuccess.postValue(it)
                        getEventCounter(id)
                    } ?: run {
                        videoError.postValue(context.resources.getString(R.string.no_news_data))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) videoError.postValue(errorMessage.detail)
                    else videoError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> videoError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }

    fun setVideoBrakeCall(isBrakeCall: Boolean) {
        videoMapperUseCaseImpl.setBrakeCall(isBrakeCall)
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
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun addLike(id: String) {
        viewModelScope.launch {
            when (val response = kebabUseCase.addLike(id)) {
                is Resource.Success -> {
//                    getEventCounter(id)
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun deleteLike(id: String) {
        viewModelScope.launch {
            when (val response = kebabUseCase.deleteLike(id)) {
                is Resource.Success -> {
//                    getEventCounter(id)
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
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
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiReportErrorMessage(response.errorData)
                    if (errorMessage?.newsGlobalId != null) eventError.postValue(errorMessage.newsGlobalId!!.toText())
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
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
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
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
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
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
            when (val response = videoUseCase.deleteNews(id)) {
                is Resource.Success -> {
                    deleteResponseSuccess.postValue(id)
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiUpdateNewsErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) videoError.postValue(errorMessage.detail)
                    else videoError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> videoError.postValue(response.errorMessage)
            }
        }
    }

    fun isFollowing(globalId: String) {
        viewModelScope.launch {
            when (val response = publisherUseCase.isFollowing(globalId)) {
                is Resource.Success -> {
                    response.data?.let {
                        isFollowing.postValue(mapOf(globalId to it))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) videoError.postValue(errorMessage.detail)
                    else videoError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> videoError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }

    fun getPublisherId(globalId: String) {
        viewModelScope.launch {
            when (val response = profileUseCase.getUserProfile(globalId)) {
                is Resource.Success -> {
                    response.data?.let {
                        publisherId.postValue(mapOf(globalId to it.id.orZero()))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) videoError.postValue(errorMessage.detail)
                    else videoError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> videoError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
            }
        }
    }

    fun subscribePublisher(publisherId: Long) {
        viewModelScope.launch {
            when (val response = useCase.addFollowing(publisherId)) {
                is Resource.Success -> {
                    response.data?.let {
//                        publisherSuccess.postValue(it)
                    } ?: run {
//                        publisherError.postValue(context.resources.getString(R.string.no_followers_data))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) videoError.postValue(apiErrorMessage.detail)
                    else videoError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> videoError.postValue(context.resources.getString(R.string.no_followers_data))
            }
        }
    }

    fun unsubscribePublisher(publisherId: Long) {
        viewModelScope.launch {
            when (val response = useCase.deleteFollowing(publisherId)) {
                is Resource.Success -> {
                    response.data?.let {
//                        publisherSuccess.postValue(it)
                    } ?: run {
//                        publisherError.postValue(context.resources.getString(R.string.no_followers_data))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        generalErrorMapperUseCaseImpl.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) videoError.postValue(apiErrorMessage.detail)
                    else videoError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> videoError.postValue(context.resources.getString(R.string.no_followers_data))
            }
        }
    }
}