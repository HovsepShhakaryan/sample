package com.hovsep.camera.createvideo.domain.usecase.impl

import android.net.Uri
import com.vylo.main.responsemain.createvideo.domain.entity.response.ParametersOfUpload
import com.vylo.main.responsemain.createvideo.domain.entity.response.VideoPresignData
import com.hovsep.camera.createvideo.domain.usecase.GetParametersFromUrlUseCase
import com.vylo.main.responsemain.createvideo.domain.entity.response.ThumbnailPresignData

class GetParametersFromUrlUseCaseImpl : GetParametersFromUrlUseCase {

    override fun getParameters(videoPresignData: VideoPresignData): ParametersOfUpload {
        val uri = Uri.parse(videoPresignData.presignedUrl)
        return ParametersOfUpload(
            host = "https://${uri.host}/",
            path = uri.path!!.replace("/", ""),
            awsAccessKeyId = uri.getQueryParameter("AWSAccessKeyId"),
            signature = uri.getQueryParameter("Signature"),
            expires = uri.getQueryParameter("Expires"),
            newsRecordId = videoPresignData.newsRecordId,
            newsRecordGlobalId = videoPresignData.newsRecordGlobalId,
            mediaRecordId = videoPresignData.mediaRecordId
        )
    }

    override fun getParameters(thumbnailPresignData: ThumbnailPresignData): ParametersOfUpload {
        val uri = Uri.parse(thumbnailPresignData.presignedUrl)
        return ParametersOfUpload(
            host = "https://${uri.host}/",
            path = uri.path!!.drop(1),
            awsAccessKeyId = uri.getQueryParameter("AWSAccessKeyId"),
            signature = uri.getQueryParameter("Signature"),
            expires = uri.getQueryParameter("Expires")
        )
    }
}

