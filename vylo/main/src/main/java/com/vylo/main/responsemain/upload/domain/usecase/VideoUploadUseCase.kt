package com.vylo.main.responsemain.upload.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.responsemain.upload.domain.entity.request.ColumnNews
import com.vylo.main.responsemain.upload.domain.entity.response.ColumnNewsData
import okhttp3.RequestBody
import okhttp3.ResponseBody

interface VideoUploadUseCase {
    suspend fun uploadVideo(path: String,
                            awsAccessKeyId: String,
                            signature: String,
                            expires: String,
                            video: RequestBody
    ): Resource<ResponseBody>

    suspend fun uploadAudio(path: String,
                            awsAccessKeyId: String,
                            signature: String,
                            expires: String,
                            video: RequestBody
    ): Resource<ResponseBody>

    suspend fun updateColumns(globalId: String,
                              columnNews: ColumnNews
    ): Resource<ColumnNewsData>

    suspend fun uploadThumbnail(path: String,
                                awsAccessKeyId: String,
                                signature: String,
                                expires: String,
                                thumbnail: RequestBody
    ): Resource<ResponseBody>
}