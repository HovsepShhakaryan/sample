package com.vylo.signup.signupprofileimage.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.signup.R
import com.vylo.signup.signupprofileimage.domain.entity.response.ProfilePhotoItem
import com.vylo.signup.signupprofileimage.domain.usecase.ProfilePickUseCase
import com.vylo.signup.signupprofileimage.domain.usecase.impl.ProfilePickUseCaseImpl
import kotlinx.coroutines.launch
import java.io.File

class SignUpPickUpProfilePictureFragmentViewModel(
    private val profilePickUseCase: ProfilePickUseCaseImpl,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responsePhotoSuccess: SingleLiveEvent<ProfilePhotoItem> by lazy { SingleLiveEvent() }
    val responsePhotoError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun updateProfilePhoto(file: File) {
        viewModelScope.launch {
            when (val response = profilePickUseCase.updateProfilePhoto(file)) {
                is Resource.Success -> {
                    response.data?.let {
                        responsePhotoSuccess.postValue(it)
                    } ?: run {
                        responsePhotoError.postValue(context.resources.getString(R.string.edit_profile_error))
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(response.errorData)
                    if (apiErrorMessage?.detail != null) responsePhotoError.postValue(apiErrorMessage.detail)
                    else responsePhotoError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responsePhotoError.postValue(response.errorMessage)
            }
        }
    }
}