package com.vylo.main.navigation.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.main.responsemain.createvideo.domain.entity.response.ParametersOfUpload
import com.hovsep.camera.createvideo.domain.usecase.impl.GetParametersFromUrlUseCaseImpl
import com.vylo.main.responsemain.createvideo.domain.usecase.impl.PresignedVideoUploadUseCaseImpl
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.R
import kotlinx.coroutines.launch

class NavigationFragmentsViewModel(
    private val videoUploadUseCaseImpl: PresignedVideoUploadUseCaseImpl,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    private val getParametersFromUrlUseCase: GetParametersFromUrlUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    private val videoFormat = context.resources.getString(R.string.label_video_format)
    private val audioFormat = context.resources.getString(R.string.label_audio_format)
    private val fileType = context.resources.getString(R.string.label_file_type)
    private val fileTypeAudio = context.resources.getString(R.string.label_audio_file_type)
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseVideoParametersSuccess: SingleLiveEvent<ParametersOfUpload> by lazy { SingleLiveEvent() }
    val responseAudioParametersSuccess: SingleLiveEvent<ParametersOfUpload> by lazy { SingleLiveEvent() }

    fun getVideoPresignData(duration: Int? = null, width: Int? = null, height: Int? = null) {
        viewModelScope.launch {
            when (val presignData =
                videoUploadUseCaseImpl.invokeVideoPresignedData(
                    videoFormat,
                    duration,
                    width,
                    height,
                    fileType
                )) {
                is Resource.Success -> {
                    if (presignData.data != null) {
                        let {
                            val parameters =
                                getParametersFromUrlUseCase.getParameters(presignData.data!!)
                            responseVideoParametersSuccess.postValue(parameters)
                        }
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(presignData.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(presignData.errorMessage)
            }
        }
    }

    fun getAudioPresignData(duration: Int? = null, width: Int? = null, height: Int? = null) {
        viewModelScope.launch {
            val presignData =
                videoUploadUseCaseImpl.invokeVideoPresignedData(
                    audioFormat,
                    duration,
                    width,
                    height,
                    fileTypeAudio
                )
            when (presignData) {
                is Resource.Success -> {
                    if (presignData.data != null) {
                        let {
                            val parameters =
                                getParametersFromUrlUseCase.getParameters(presignData.data!!)
                            responseAudioParametersSuccess.postValue(parameters)
                        }
                    }
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(presignData.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(presignData.errorMessage)
            }
        }
    }

}