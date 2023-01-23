package com.vylo.main.responsemain.createvideo.domain.usecase.impl

import com.vylo.main.responsemain.createvideo.data.repository.impl.VideoRepositoryImpl
import com.hovsep.camera.createvideo.domain.usecase.PresignedVideoUploadUseCase
import com.vylo.common.api.Resource
import com.vylo.main.responsemain.createvideo.domain.entity.response.VideoPresignData
import com.vylo.main.responsemain.upload.domain.entity.request.Thumbnail

class PresignedVideoUploadUseCaseImpl(
    private val videoRepositoryImpl: VideoRepositoryImpl
) : PresignedVideoUploadUseCase {

    override suspend fun invokeVideoPresignedData(
        videoFormat: String,
        duration: Int?,
        width: Int?,
        height: Int?,
        fileType: String
    ) = videoRepositoryImpl.invokeVideoPresignedData(videoFormat, duration, width, height, fileType)

    override suspend fun invokeThumbnailPresignedData(
        postId: String,
        thumbnail: Thumbnail
    ) = videoRepositoryImpl.invokeThumbnailPresignedData(postId, thumbnail)

}