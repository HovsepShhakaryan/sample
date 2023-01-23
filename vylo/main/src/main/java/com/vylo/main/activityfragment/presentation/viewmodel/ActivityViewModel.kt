package com.vylo.main.activityfragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.main.R
import com.vylo.main.activityfragment.domain.entity.response.ActivityItem
import com.vylo.main.activityfragment.domain.usecase.ActivityUseCase
import com.vylo.main.activityfragment.domain.usecase.impl.ActivityMapperUseCaseImpl
import com.vylo.main.component.entity.toPublishersEntity
import com.vylo.main.component.kebab.domain.usecase.KebabUseCase
import com.vylo.main.following.domain.usecase.PublisherUseCase
import com.vylo.main.profilefragment.domain.usecase.ProfileUseCase
import kotlinx.coroutines.launch

class ActivityViewModel(
    private val useCase: ActivityUseCase,
    private val kebabUseCase: KebabUseCase,
    private val profileUseCase: ProfileUseCase,
    private val publisherUseCase: PublisherUseCase,
    private val mapperUseCase: GeneralErrorMapperUseCase,
    private val pagingUseCase: ActivityMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    var isActivityFirstTime = true

    val activitySuccess: SingleLiveEvent<List<ActivityItem>> by lazy { SingleLiveEvent() }
    val activityError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val eventError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val isEnableDataCall: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent() }

    val followingIds: SingleLiveEvent<List<String>> by lazy { SingleLiveEvent() }

    private var actualData: List<ActivityItem>? = null

    fun setActualData(actualData: List<ActivityItem>) {
        this.actualData = actualData
    }

    fun getActualData() = this.actualData

    fun getActivity() {
        isActivityFirstTime = false
        viewModelScope.launch {
            if (!pagingUseCase.isBrakeCall()) {
                when (val response = useCase.getActivity(pagingUseCase.getPagingToken())) {
                    is Resource.Success -> {
                        response.data?.let {
                            val item = pagingUseCase.fromActivityDataToActivityItemList(it)
                            activitySuccess.postValue(item)
                            isEnableDataCall.postValue(!pagingUseCase.isBrakeCall())
                        } ?: run {
                            activityError.postValue(context.resources.getString(R.string.no_activity_data))
                        }
                    }
                    is Resource.ApiError -> {
                        mapperUseCase.getApiErrorMessage(response.errorData)?.let {
                            activityError.postValue(it.detail)
                        }
                    }
                    is Resource.Error -> {
                        activityError.postValue(response.errorMessage)
                    }
                }
            }
        }
    }

    fun followUser(id: String, isFollow: Boolean) {
        viewModelScope.launch {
            when (val response = profileUseCase.getUserProfile(id)) {
                is Resource.Success -> {
                    response.data?.id?.let { userId ->
                        if (isFollow) {
                            follow(userId)
                        } else {
                            unfollow(userId)
                        }
                    }
                }
                is Resource.ApiError -> {
                    mapperUseCase.getApiErrorMessage(response.errorData)?.let {
                        activityError.postValue(it.detail)
                    }
                }
                is Resource.Error -> {
                    activityError.postValue(response.errorMessage)
                }
            }
        }
    }

    fun setActivityBrakeCall(isBrakeCall: Boolean) {
        pagingUseCase.setBrakeCall(isBrakeCall)
    }

    private fun follow(id: Long) {
        viewModelScope.launch {
            when (val response = profileUseCase.addPublisher(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        it.publishersSubscription?.find { it.id == id }?.let { item ->
                            publisherUseCase.save(item.toPublishersEntity())
                        }
                    } ?: run {
                        activityError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    mapperUseCase.getApiErrorMessage(response.errorData)?.let {
                        activityError.postValue(it.detail)
                    }
                }
                is Resource.Error -> activityError.postValue(response.errorMessage)
            }
        }
    }

    private fun unfollow(id: Long) {
        viewModelScope.launch {
            when (val response = profileUseCase.deletePublisher(id)) {
                is Resource.Success -> {
                    response.data?.let {
                        publisherUseCase.delete(id)
                    } ?: run {
                        activityError.postValue(response.errorMessage)
                    }
                }
                is Resource.ApiError -> {
                    mapperUseCase.getApiErrorMessage(response.errorData)?.let {
                        activityError.postValue(it.detail)
                    }
                }
                is Resource.Error -> activityError.postValue(response.errorMessage)
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
                        mapperUseCase.getApiErrorMessage(response.errorData)
                    errorMessage?.detail.let {
                        eventError.postValue(it)
                    }
                }
                is Resource.Error -> {
                    eventError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                }
            }
        }
    }

    fun getFollowings() {
        viewModelScope.launch {
            when (val response = publisherUseCase.getPublishers()) {
                is Resource.Success -> {
                    response.data?.let {
                        followingIds.postValue(it.sortedBy { it.globalId }
                            .map { it.globalId.orEmpty() })
                    }
                }
                is Resource.ApiError -> {
                    mapperUseCase.getApiErrorMessage(response.errorData)?.let {
                        activityError.postValue(it.detail)
                    }
                }
                is Resource.Error -> activityError.postValue(response.errorMessage)
            }
        }
    }
}