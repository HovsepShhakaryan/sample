package com.vylo.main.responsemain.createvideo.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoPresignData(
    @SerializedName("news_record_id")
    val newsRecordId: Long? = null,
    @SerializedName("news_record_global_id")
    val newsRecordGlobalId: String? = null,
    @SerializedName("media_record_id")
    val mediaRecordId: Long? = null,
    @SerializedName("file_name")
    val fileName: String? = null,
    @SerializedName("presigned_url")
    val presignedUrl: String? = null
) : Parcelable
