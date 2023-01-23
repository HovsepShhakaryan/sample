package com.vylo.main.responsemain.createvideo.data.repository.impl

import android.content.Context
import com.vylo.main.responsemain.createvideo.data.endpoint.VideoApi
import com.hovsep.camera.createvideo.data.repository.VideoRepository
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.responsemain.upload.domain.entity.request.Thumbnail
import org.koin.java.KoinJavaComponent

class VideoRepositoryImpl(
    context: Context
) : BaseRepo(), VideoRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(VideoApi::class.java)

    override suspend fun invokeVideoPresignedData(
        videoFormat: String,
        duration: Int?,
        width: Int?,
        height: Int?,
        fileType: String
    ) = safeApiCall {
        client.invokeVideoPresignedData(
            videoFormat,
            duration,
            width,
            height,
            fileType
        )
    }

    override suspend fun invokeThumbnailPresignedData(
        postId: String,
        thumbnail: Thumbnail,
    ) = safeApiCall {
        client.invokeThumbnailPresignedData(
            postId,
            thumbnail
        )
    }
}