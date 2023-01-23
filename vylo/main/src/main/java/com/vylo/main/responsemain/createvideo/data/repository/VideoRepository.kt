package com.hovsep.camera.createvideo.data.repository

import com.vylo.main.responsemain.createvideo.domain.entity.response.VideoPresignData
import com.vylo.common.api.Resource
import com.vylo.main.responsemain.createvideo.domain.entity.response.ThumbnailPresignData
import com.vylo.main.responsemain.upload.domain.entity.request.Thumbnail

interface VideoRepository {
    suspend fun invokeVideoPresignedData(
        videoFormat: String,
        duration: Int?,
        width: Int?,
        height: Int?,
        fileType: String
    ): Resource<VideoPresignData>

    suspend fun invokeThumbnailPresignedData(
        postId: String,
        thumbnail: Thumbnail,
    ): Resource<ThumbnailPresignData>
}