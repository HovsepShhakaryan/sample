package com.vylo.main.responsemain.createvideo.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThumbnailPresignData(
    @SerializedName("presigned_url")
    val presignedUrl: String? = null
) : Parcelable