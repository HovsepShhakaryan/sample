package com.vylo.main.followmain.followfragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.main.R
import com.vylo.main.component.entity.ProfileItem
import com.vylo.main.component.entity.PublishersSubscription
import com.vylo.main.followmain.followfragment.domain.usecase.FollowUseCase
import com.vylo.main.followmain.followfragment.domain.usecase.impl.FollowMapperUseCaseImpl
import kotlinx.coroutines.launch

class FollowViewModel(
    private val useCase: FollowUseCase,
    private val mapperUseCase: GeneralErrorMapperUseCase,
    private val followMapperUseCase: FollowMapperUseCaseImpl,
    private val followingMapperUseCase: FollowMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application

    var isFollowerFirstTime = true
    var isFollowingFirstTime = true

    val publisherSuccess: SingleLiveEvent<ProfileItem> by lazy { SingleLiveEvent() }
    val publisherError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val followerSuccess: SingleLiveEvent<List<PublishersSubscription>> by lazy { SingleLiveEvent() }
    val followerError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val followingSuccess: SingleLiveEvent<List<PublishersSubscription>> by lazy { SingleLiveEvent() }
    val followingError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun getFollowers(id: String? = null) {
        isFollowerFirstTime = false
        viewModelScope.launch {
            if (!followMapperUseCase.isBrakeCall()) {
                when (val response = useCase.getFollowers(
                    id,
                    FOLLOWERS_PAGE_SIZE,
                    followMapperUseCase.getPagingToken()
                )) {
                    is Resource.Success -> {
                        response.data?.let {
                            val publishersSubscriptionData =
                                followMapperUseCase.fromPublishersSubscriptionDataToPublishersSubscriptionList(
                                    response.data!!
                                )
                            followerSuccess.postValue(publishersSubscriptionData)
                        } ?: run {
                            followerError.postValue(context.resources.getString(R.string.no_followers_data))
                        }
                    }
                    is Resource.ApiError -> {
                        val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                        if (apiErrorMessage?.detail != null) followerError.postValue(apiErrorMessage.detail)
                        else followerError.postValue(context.resources.getString(R.string.label_something_wrong))
                    }
                    is Resource.Error -> {
                        followerError.postValue(response.errorMessage)
                    }
                }
            } else {
                followerError.postValue(context.resources.getString(R.string.no_followers_data))
            }
        }
    }

    fun getFollowings(id: String? = null) {
        isFollowingFirstTime = false
        viewModelScope.launch {
            if (!followingMapperUseCase.isBrakeCall()) {
                when (val response = useCase.getFollowing(
                    id,
                    FOLLOWINGS_PAGE_SIZE,
                    followingMapperUseCase.getPagingToken()
                )) {
                    is Resource.Success -> {
                        response.data?.let {
                            val publishersSubscriptionData =
                                followingMapperUseCase.fromPublishersSubscriptionDataToPublishersSubscriptionList(
                                    response.data!!
                                )
                            followingSuccess.postValue(publishersSubscriptionData)
                        } ?: run {
                            followingError.postValue(context.resources.getString(R.string.no_followings_data))
                        }
                    }
                    is Resource.ApiError -> {
                        val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                        if (apiErrorMessage?.detail != null) followerError.postValue(apiErrorMessage.detail)
                        else followerError.postValue(context.resources.getString(R.string.label_something_wrong))
                    }
                    is Resource.Error -> {
                        followingError.postValue(response.errorMessage)
                    }
                }
            } else {
                followingError.postValue(context.resources.getString(R.string.no_followings_data))
            }
        }
    }

    fun subscribePublisher(publisherId: Long) {
        viewModelScope.launch {
            when (val response = useCase.addFollowing(publisherId)) {
                is Resource.Success -> {
                    response.data?.let {
                        publisherSuccess.postValue(it)
                    } ?: run {
//                        publisherError.postValue(context.resources.getString(R.string.no_followers_data))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) publisherError.postValue(apiErrorMessage.detail)
                    else publisherError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> publisherError.postValue(context.resources.getString(R.string.no_followers_data))
            }
        }
    }

    fun unsubscribePublisher(publisherId: Long) {
        viewModelScope.launch {
            when (val response = useCase.deleteFollowing(publisherId)) {
                is Resource.Success -> {
                    response.data?.let {
                        publisherSuccess.postValue(it)
                    } ?: run {
//                        publisherError.postValue(context.resources.getString(R.string.no_followers_data))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = mapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) publisherError.postValue(apiErrorMessage.detail)
                    else publisherError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> publisherError.postValue(context.resources.getString(R.string.no_followers_data))
            }
        }
    }

    fun setFollowerBrakeCall(isBrakeCall: Boolean) {
        followMapperUseCase.setBrakeCall(isBrakeCall)
    }

    fun setFollowingBrakeCall(isBrakeCall: Boolean) {
        followingMapperUseCase.setBrakeCall(isBrakeCall)
    }

    companion object {
        const val FOLLOWERS_PAGE_SIZE = 20
        const val FOLLOWINGS_PAGE_SIZE = 20
    }
}