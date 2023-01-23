package com.vylo.main.responsemain.upload.data.endpoint

import com.vylo.common.api.HeaderExcluder
import com.vylo.main.responsemain.upload.domain.entity.request.ColumnNews
import com.vylo.main.responsemain.upload.domain.entity.response.ColumnNewsData
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface VideoUploadApi {

    @Headers("${HeaderExcluder.isToken}: false")
    @PUT("{endpoint}")
    suspend fun uploadFile(
        @Path("endpoint") path: String,
        @Query("AWSAccessKeyId") awsAccessKeyId: String,
        @Query("Signature") signature: String,
        @Query("Expires") expires: String,
        @Body video: RequestBody
    ): Response<ResponseBody>

    @PUT("$COLUMNS_UPDATE_CALL{global_id}")
    suspend fun updateColumns(
        @Path("global_id") globalId: String,
        @Body columnNews: ColumnNews
    ): Response<ColumnNewsData>

    @Headers("${HeaderExcluder.isToken}: false")
    @PUT("{path1}/{path2}")
    suspend fun uploadThumbnail(
        @Path("path1") path1: String,
        @Path("path2") path2: String,
        @Query("AWSAccessKeyId") awsAccessKeyId: String,
        @Query("Signature") signature: String,
        @Query("Expires") expires: String,
        @Body thumbnail: RequestBody
    ): Response<ResponseBody>
}