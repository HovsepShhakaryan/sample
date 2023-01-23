package com.vylo.main.profilefragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.MyProfileUseCase
import com.vylo.common.ext.toText
import com.vylo.main.R
import com.vylo.main.component.entity.ProfileItem
import com.vylo.main.component.entity.toPublishersEntity
import com.vylo.main.component.kebab.domain.entity.request.BlockUser
import com.vylo.main.component.kebab.domain.entity.request.ReportRequest
import com.vylo.main.component.kebab.domain.entity.response.EventCounterData
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.following.domain.usecase.PublisherUseCase
import com.vylo.main.profilefragment.domain.entity.request.NewsUpdateRequest
import com.vylo.main.profilefragment.domain.entity.response.NewsResponse
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsItem
import com.vylo.main.profilefragment.domain.usecase.ProfileUseCase
import com.vylo.main.profilefragment.domain.usecase.impl.ProfileMapperUseCaseImpl
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val useCase: ProfileUseCase,
    private val errorUseCase: GeneralErrorMapperUseCase,
    private val myProfileUseCase: MyProfileUseCase,
    private val profileMapperUseCaseImpl: ProfileMapperUseCaseImpl,
    private val publisherUseCase: PublisherUseCase,
    private val kebabUseCase: KebabUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseProfileSuccess: SingleLiveEvent<ProfileItem> by lazy { SingleLiveEvent() }
    val responseProfileError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseMyProfileSuccess: SingleLiveEvent<ProfileItem> by lazy { SingleLiveEvent() }
    val responseMyProfileError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseNewsSuccess: SingleLiveEvent<List<ProfileNewsItem>> by lazy { SingleLiveEvent() }
    val responseNewsError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val updateResponseSuccess: SingleLiveEvent<NewsResponse> by lazy { SingleLiveEvent() }
    val deleteResponseSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val eventError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val eventSuccess: SingleLiveEvent<EventCounterData> by lazy { SingleLiveEvent() }

    val myGlobalId: String?
        get() = myProfileUseCase.myGlobalId

    private var actualData: List<ProfileNewsItem>? = null
    fun setActualData(actualData: List<ProfileNewsItem>) {
        this.actualData = actualData
    }

    fun getActualData() = actualData

    fun getProfile() {
        viewModelScope.launch {
            when (val response = useCase.getProfile()) {
                is Resource.Success -> {
                    response.data?.let {
                        responseProfileSuccess.postValue(it)
                        publisherUseCase.deleteAll()
                        it.publishersSubscription?.map { it.toPublishersEntity() }?.let {
                            publisherUseCase.saveAll(it)
                        }
                    } ?: run {
                        responseProfileError.postValue(context.resources.getString(R.string.no_profile_data))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responseProfileError.postValue(errorMessage.detail)
                    else responseProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseProfileError.postValue(response.errorMessage)
            }
        }
    }

    fun getMyProfile() {
        viewModelScope.launch {
            when (val response = useCase.getProfile()) {
                is Resource.Success -> {
                    response.data?.let {
                        responseMyProfileSuccess.postValue(it)
                    } ?: run {
                        responseMyProfileError.postValue(context.resources.getString(R.string.no_profile_data))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responseMyProfileError.postValue(errorMessage.detail)
                    else responseMyProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseMyProfileError.postValue(response.errorMessage)
            }
        }
    }

    fun getUserProfile(id: String) {
        viewModelScope.launch {
            when (val response = useCase.getUserProfile(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        responseProfileSuccess.postValue(it)
                    } ?: run {
                        responseProfileError.postValue(context.resources.getString(R.string.no_profile_data))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responseProfileError.postValue(errorMessage.detail)
                    else responseProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseProfileError.postValue(response.errorMessage)
            }
        }
    }

    fun getProfileNews() {
        viewModelScope.launch {
            if (!profileMapperUseCaseImpl.isBrakeCall()) {
                when (val response = useCase.getProfileNews(null, profileMapperUseCaseImpl.getPagingToken())) {
                    is Resource.Success -> {
                        response.data?.let {
                            val profileData =
                                profileMapperUseCaseImpl.fromProfileNewsDataToProfileNewsItemList(response.data!!)
                            responseNewsSuccess.postValue(profileData?.filter { it.status == 2 || it.status == 3 || it.status == 4 })
                        } ?: run {
                            responseNewsError.postValue(context.resources.getString(R.string.no_profile_news))
                        }
                    }
                    is Resource.ApiError -> {
                        val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                        if (errorMessage?.detail != null) responseNewsError.postValue(errorMessage.detail)
                        else responseNewsError.postValue(context.resources.getString(R.string.label_something_wrong))
                    }
                    is Resource.Error -> {
                        responseNewsError.postValue(response.errorMessage)
                    }
                }
            }
        }
    }

    fun getUserProfileNews(id: String) {
        viewModelScope.launch {
            if (!profileMapperUseCaseImpl.isBrakeCall()) {
                when (val response =
                    useCase.getUserProfileNews(id, profileMapperUseCaseImpl.getPagingToken())) {
                    is Resource.Success -> {
                        response.data?.let {
                            val profileData =
                                profileMapperUseCaseImpl.fromProfileNewsDataToProfileNewsItemList(response.data!!)
                            responseNewsSuccess.postValue(profileData)
                        } ?: run {
                            responseNewsError.postValue(context.resources.getString(R.string.no_profile_news))
                        }
                    }
                    is Resource.ApiError -> {
                        val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                        if (errorMessage?.detail != null) responseNewsError.postValue(errorMessage.detail)
                        else responseNewsError.postValue(context.resources.getString(R.string.label_something_wrong))
                    }
                    is Resource.Error -> {
                        responseNewsError.postValue(response.errorMessage)
                    }
                }
            }
        }
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
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun setBrakeCall(isBrakeCall: Boolean) {
        profileMapperUseCaseImpl.setBrakeCall(isBrakeCall)
    }

    fun follow(id: Long) {
        viewModelScope.launch {
            when (val response = useCase.addPublisher(id)) {
                is Resource.Success -> {
                    response.data?.let {

                    } ?: run {
                        responseProfileError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responseProfileError.postValue(errorMessage.detail)
                    else responseProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseProfileError.postValue(response.errorMessage)
            }
        }
    }

    fun unfollow(id: Long) {
        viewModelScope.launch {
            when (val response = useCase.deletePublisher(id)) {
                is Resource.Success -> {
                    response.data?.let {

                    } ?: run {
                        responseProfileError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responseProfileError.postValue(errorMessage.detail)
                    else responseProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseProfileError.postValue(response.errorMessage)
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
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
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
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) eventError.postValue(errorMessage.detail)
                    else eventError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun updateNews(id: String, data: NewsUpdateRequest) {
        viewModelScope.launch {
            when (val response = useCase.updateNews(id, data)) {
                is Resource.Success -> {
                    response.data?.let {
                        updateResponseSuccess.postValue(it)
                    } ?: run {
                        responseProfileError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = errorUseCase.getApiUpdateNewsErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responseProfileError.postValue(errorMessage.detail)
                    else responseProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseProfileError.postValue(response.errorMessage)
            }
        }
    }

    fun deleteNews(id: String) {
        viewModelScope.launch {
            when (val response = useCase.deleteNews(id)) {
                is Resource.Success -> {
                    deleteResponseSuccess.postValue(id)
                }
                is Resource.ApiError -> {
                    val errorMessage = errorUseCase.getApiUpdateNewsErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responseProfileError.postValue(errorMessage.detail)
                    else responseProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseProfileError.postValue(response.errorMessage)
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
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responseProfileError.postValue(errorMessage.detail)
                    else responseProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    responseProfileError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
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
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
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
                    val apiErrorMessage =
                        errorUseCase.getApiReportErrorMessage(response.errorData)
                    if (apiErrorMessage?.newsGlobalId != null) responseProfileError.postValue(
                        apiErrorMessage.newsGlobalId!!.toText()
                    )
                    else responseProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    responseProfileError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun blockUser(globalId: String) {
        viewModelScope.launch {
            val blockUser = BlockUser(globalId)
            when (val response = kebabUseCase.blockUser(blockUser)) {
                is Resource.Success -> {

                }
                is Resource.ApiError -> {
                    val apiErrorMessage =
                        errorUseCase.getApiReportErrorMessage(response.errorData)
                    if (apiErrorMessage?.newsGlobalId != null) responseProfileError.postValue(
                        apiErrorMessage.newsGlobalId!!.toText()
                    )
                    else responseProfileError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> {
                    responseProfileError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }
}