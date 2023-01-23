package com.vylo.main.responsemain.upload.presentation.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hovsep.camera.createvideo.domain.usecase.impl.GetParametersFromUrlUseCaseImpl
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.main.R
import com.vylo.main.responsemain.createvideo.domain.entity.response.ParametersOfUpload
import com.vylo.main.responsemain.createvideo.domain.usecase.impl.PresignedVideoUploadUseCaseImpl
import com.vylo.main.responsemain.upload.domain.entity.request.Thumbnail
import com.vylo.main.responsemain.upload.domain.livedata.NoParameter
import com.vylo.main.responsemain.upload.domain.usecase.impl.ColumnNewsUseCaseImpl
import com.vylo.main.responsemain.upload.domain.usecase.impl.VideoUploadUseCaseImpl
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class UploadFragmentViewModel(
    private val videoUploadUseCaseImpl: VideoUploadUseCaseImpl,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    private val columnNewsUseCaseImpl: ColumnNewsUseCaseImpl,
    private val presignedVideoUploadUseCaseImpl: PresignedVideoUploadUseCaseImpl,
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
    val responseUploadError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseUploadSuccess: SingleLiveEvent<NoParameter> by lazy { SingleLiveEvent() }
    val responseThumbnailPresignedUrl: SingleLiveEvent<ParametersOfUpload> by lazy { SingleLiveEvent() }

    fun getVideoPresignData(duration: Int?, width: Int?, height: Int?) {
        viewModelScope.launch {
            when (val presignData =
                presignedVideoUploadUseCaseImpl.invokeVideoPresignedData(
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

    fun getThumbnailPresignData(postId: String, thumbnail: Thumbnail) {
        viewModelScope.launch {
            when (val presignData =
                presignedVideoUploadUseCaseImpl.invokeThumbnailPresignedData(
                    postId,
                    thumbnail
                )) {
                is Resource.Success -> {
                    if (presignData.data != null) {
                        let {
                            val parameters = getParametersFromUrlUseCase.getParameters(presignData.data!!)
                            responseThumbnailPresignedUrl.postValue(parameters)
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

    fun getAudioPresignData(duration: Int?) {
        viewModelScope.launch {
            val presignData =
                presignedVideoUploadUseCaseImpl.invokeVideoPresignedData(
                    audioFormat,
                    duration,
                    null,
                    null,
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

    fun uploadVideo(
        multipartBody: RequestBody,
        parametersOfFileUpload: ParametersOfUpload
    ) {
        viewModelScope.launch {
            val uploadResponse = videoUploadUseCaseImpl.uploadVideo(
                parametersOfFileUpload.path!!,
                parametersOfFileUpload.awsAccessKeyId!!,
                parametersOfFileUpload.signature!!,
                parametersOfFileUpload.expires!!,
                multipartBody,
            )
            when (uploadResponse) {
                is Resource.Success -> {

                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(uploadResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseUploadError.postValue(apiErrorMessage.detail)
                    else responseUploadError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseUploadError.postValue(uploadResponse.errorMessage)
            }

        }
    }

    fun uploadThumbnail(
        parametersOfFileUpload: ParametersOfUpload,
        multipartBody: RequestBody
    ) {
        viewModelScope.launch {
            val uploadResponse = videoUploadUseCaseImpl.uploadThumbnail(
                parametersOfFileUpload.path!!,
                parametersOfFileUpload.awsAccessKeyId!!,
                parametersOfFileUpload.signature!!,
                parametersOfFileUpload.expires!!,
                multipartBody
            )
            when (uploadResponse) {
                is Resource.Success -> {

                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(uploadResponse.errorData)
                    if (apiErrorMessage?.detail != null) {
                        responseUploadError.postValue(apiErrorMessage.detail)
                        println(apiErrorMessage.detail)
                    } else {
                        responseUploadError.postValue(context.resources.getString(R.string.label_something_wrong))
                    }
                }
                is Resource.Error -> responseUploadError.postValue(uploadResponse.errorMessage)
            }

        }
    }

    fun uploadAudio(
        multipartBody: RequestBody,
        parametersOfFileUpload: ParametersOfUpload
    ) {
        viewModelScope.launch {
            val uploadResponse = videoUploadUseCaseImpl.uploadAudio(
                parametersOfFileUpload.path!!,
                parametersOfFileUpload.awsAccessKeyId!!,
                parametersOfFileUpload.signature!!,
                parametersOfFileUpload.expires!!,
                multipartBody,
            )
            when (uploadResponse) {
                is Resource.Success -> {

                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(uploadResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseUploadError.postValue(apiErrorMessage.detail)
                    else responseUploadError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseUploadError.postValue(uploadResponse.errorMessage)
            }

        }
    }

    fun updateColumns(
        globalId: String
    ) {
        viewModelScope.launch {
            val updateColumns = videoUploadUseCaseImpl.updateColumns(
                globalId,
                columnNewsUseCaseImpl.createColumnNewsModel()
            )
            when (updateColumns) {
                is Resource.Success -> {
                    if (updateColumns.data != null)
                        responseUploadSuccess.postValue(NoParameter.ColumnNews(updateColumns.data!!))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(updateColumns.errorData)
                    if (apiErrorMessage?.detail != null) responseUploadError.postValue(apiErrorMessage.detail)
                    else responseUploadError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseUploadError.postValue(updateColumns.errorMessage)
            }
        }
    }

    fun setCategory(category: String) {
        columnNewsUseCaseImpl.setCategory(category)
    }

    fun setTitle(title: String) {
        columnNewsUseCaseImpl.setTitle(title)
    }

    fun setDescription(description: String) {
        columnNewsUseCaseImpl.setDescription(description)
    }

    fun setLink(link: String) {
        columnNewsUseCaseImpl.setLink(link)
    }

    fun setAuthor(author: String) {
        columnNewsUseCaseImpl.setAuthor(author)
    }

    fun setResponseTo(responseTo: String) {
        columnNewsUseCaseImpl.setResponseTo(responseTo)
    }

    fun clearResponseTo() {
        columnNewsUseCaseImpl.clearResponseTo()
    }

    fun setStatus(status: Long) {
        columnNewsUseCaseImpl.setStatus(status)
    }
}