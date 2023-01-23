package com.vylo.main.responsemain.createvideo.data.endpoint

import com.vylo.main.responsemain.createvideo.domain.entity.response.ThumbnailPresignData
import com.vylo.main.responsemain.createvideo.domain.entity.response.VideoPresignData
import com.vylo.main.responsemain.upload.domain.entity.request.Thumbnail
import com.vylo.main.responsemain.upload.presentation.fragment.ThumbnailType
import retrofit2.Response
import retrofit2.http.*

interface VideoApi {

    @GET("$GENERATE_PRESIGNED_URL_CALL{format}")
    suspend fun invokeVideoPresignedData(
        @Path("format") videoFormat: String,
        @Query("duration") duration: Int?,
        @Query("width") width: Int?,
        @Query("height") height: Int?,
        @Query("file_type") fileType: String
    ): Response<VideoPresignData>

    @PUT("$GENERATE_THUMBNAIL_PRESIGNED_URL_CALL{postId}")
    suspend fun invokeThumbnailPresignedData(
        @Path("postId") postId: String,
        @Body thumbnail: Thumbnail
    ): Response<ThumbnailPresignData>

}