package com.vylo.main.responsemain.upload.data.repository.impl

import android.content.Context
import com.vylo.main.responsemain.upload.data.endpoint.VideoUploadApi
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.api.apiservis.ApiUploadAudio
import com.vylo.common.api.apiservis.ApiUploadThumbnail
import com.vylo.common.api.apiservis.ApiUploadVideo
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.responsemain.upload.data.repository.VideoUploadRepository
import com.vylo.main.responsemain.upload.domain.entity.request.ColumnNews
import okhttp3.RequestBody
import org.koin.java.KoinJavaComponent

class VideoUploadRepositoryImpl(
    context: Context
) : BaseRepo(), VideoUploadRepository {

    private val apiColumn: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val clientColumn = apiColumn.getInstance(context).create(VideoUploadApi::class.java)
    private val apiUploadVideo: ApiUploadVideo by KoinJavaComponent.inject(ApiUploadVideo::class.java)
    private val clientUploadVideo = apiUploadVideo.getInstance(context).create(VideoUploadApi::class.java)
    private val apiUploadThumbnail: ApiUploadThumbnail by KoinJavaComponent.inject(ApiUploadThumbnail::class.java)
    private val clientUploadThumbnail = apiUploadThumbnail.getInstance(context).create(VideoUploadApi::class.java)
    private val apiUploadAudio: ApiUploadAudio by KoinJavaComponent.inject(ApiUploadAudio::class.java)
    private val clientUploadAudio = apiUploadAudio.getInstance(context).create(VideoUploadApi::class.java)

    override suspend fun uploadVideo(
        path: String,
        awsAccessKeyId: String,
        signature: String,
        expires: String,
        video: RequestBody
    ) = safeApiCall {
        clientUploadVideo.uploadFile(
            path,
            awsAccessKeyId,
            signature,
            expires,
            video
        )
    }

    override suspend fun uploadAudio(
        path: String,
        awsAccessKeyId: String,
        signature: String,
        expires: String,
        video: RequestBody
    ) = safeApiCall {
        clientUploadAudio.uploadFile(
            path,
            awsAccessKeyId,
            signature,
            expires,
            video
        )
    }

    override suspend fun updateColumns(
        globalId: String,
        columnNews: ColumnNews
    ) = safeApiCall {
        clientColumn.updateColumns(
            globalId,
            columnNews
        )
    }

    override suspend fun uploadThumbnail(
        path: String,
        awsAccessKeyId: String,
        signature: String,
        expires: String,
        thumbnail: RequestBody
    ) = safeApiCall {
        clientUploadThumbnail.uploadThumbnail(
            path.split("/")[0],
            path.split("/")[1],
            awsAccessKeyId,
            signature,
            expires,
            thumbnail
        )
    }

}