package com.hovsep.camera.createvideo.domain.usecase

import com.vylo.main.responsemain.createvideo.domain.entity.response.ParametersOfUpload
import com.vylo.main.responsemain.createvideo.domain.entity.response.ThumbnailPresignData
import com.vylo.main.responsemain.createvideo.domain.entity.response.VideoPresignData

interface GetParametersFromUrlUseCase {
    fun getParameters(videoPresignData: VideoPresignData): ParametersOfUpload
    fun getParameters(thumbnailPresignData: ThumbnailPresignData): ParametersOfUpload
}