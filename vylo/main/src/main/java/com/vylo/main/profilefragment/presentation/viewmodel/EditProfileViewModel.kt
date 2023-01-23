package com.vylo.main.profilefragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.ext.toText
import com.vylo.main.R
import com.vylo.main.component.entity.ProfileItem
import com.vylo.main.component.entity.ProfilePhotoItem
import com.vylo.main.profilefragment.domain.entity.request.ProfileRequest
import com.vylo.main.profilefragment.domain.usecase.ProfileUseCase
import kotlinx.coroutines.launch
import java.io.File

class EditProfileViewModel(
    private val useCase: ProfileUseCase,
    private val errorUseCase: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseSuccess: SingleLiveEvent<ProfileItem> by lazy { SingleLiveEvent() }
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    val responsePhotoSuccess: SingleLiveEvent<ProfilePhotoItem> by lazy { SingleLiveEvent() }
    val responsePhotoError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun updateProfile(data: ProfileRequest) {
        viewModelScope.launch {
            when (val response = useCase.updateProfile(data)) {
                is Resource.Success -> {
                    response.data?.let {
                        responseSuccess.postValue(it)
                    } ?: run {
                        responseError.postValue(context.resources.getString(R.string.edit_profile_error))
                    }
                }
                is Resource.ApiError -> {
                    errorUseCase.getApiEditProfileErrorMessage(response.errorData)?.let {
                        val error = it.name ?: it.username ?: it.website ?: it.bio
                        error?.let {
                            responseError.postValue(it.toText())
                        } ?: run {
                            responseError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                        }
                    } ?: run {
                        responseError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                    }
                }
                is Resource.Error -> responseError.postValue(response.errorMessage)
            }
        }
    }

    fun updatePersonalInformation(data: ProfileRequest) {
        viewModelScope.launch {
            when (val response = useCase.updateProfile(data)) {
                is Resource.Success -> {
                    response.data?.let {
                        responseSuccess.postValue(it)
                    } ?: run {
                        responseError.postValue(context.resources.getString(R.string.edit_personal_information_error))
                    }
                }
                is Resource.ApiError -> {
                    errorUseCase.getApiPersonalInformationErrorMessage(response.errorData)?.let {
                        val error = it.email ?: it.phone ?: it.gender ?: it.birthdayDate
                        error?.let {
                            responseError.postValue(it.toText())
                        } ?: run {
                            responseError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                        }
                    } ?: run {
                        responseError.postValue(context.resources.getString(com.vylo.common.R.string.something_went_wrong))
                    }
                }
                is Resource.Error -> responseError.postValue(response.errorMessage)
            }
        }
    }

    fun updateProfilePhoto(file: File) {
        viewModelScope.launch {
            when (val response = useCase.updateProfilePhoto(file)) {
                is Resource.Success -> {
                    response.data?.let {
                        responsePhotoSuccess.postValue(it)
                    } ?: run {
                        responsePhotoError.postValue(context.resources.getString(R.string.edit_profile_error))
                    }
                }
                is Resource.ApiError -> {
                    val errorMessage = errorUseCase.getApiErrorMessage(response.errorData)
                    if (errorMessage?.detail != null) responsePhotoError.postValue(errorMessage.detail)
                    else responsePhotoError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responsePhotoError.postValue(response.errorMessage)
            }
        }
    }

}