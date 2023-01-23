package com.vylo.main.responsemain.upload.domain.usecase.impl

import com.vylo.main.responsemain.upload.data.repository.impl.VideoUploadRepositoryImpl
import com.vylo.main.responsemain.upload.domain.entity.request.ColumnNews
import com.vylo.main.responsemain.upload.domain.usecase.VideoUploadUseCase
import okhttp3.RequestBody

class VideoUploadUseCaseImpl(
    private val videoUploadRepositoryImpl: VideoUploadRepositoryImpl
) : VideoUploadUseCase {

    override suspend fun uploadVideo(
        path: String,
        awsAccessKeyId: String,
        signature: String,
        expires: String,
        video: RequestBody
    ) = videoUploadRepositoryImpl.uploadVideo(
        path,
        awsAccessKeyId,
        signature,
        expires,
        video
    )

    override suspend fun uploadAudio(
        path: String,
        awsAccessKeyId: String,
        signature: String,
        expires: String,
        video: RequestBody
    ) = videoUploadRepositoryImpl.uploadAudio(
        path,
        awsAccessKeyId,
        signature,
        expires,
        video
    )

    override suspend fun updateColumns(
        globalId: String,
        columnNews: ColumnNews
    ) = videoUploadRepositoryImpl.updateColumns(
        globalId,
        columnNews
    )

    override suspend fun uploadThumbnail(
        path: String,
        awsAccessKeyId: String,
        signature: String,
        expires: String,
        thumbnail: RequestBody
    ) = videoUploadRepositoryImpl.uploadThumbnail(
        path,
        awsAccessKeyId,
        signature,
        expires,
        thumbnail
    )
}