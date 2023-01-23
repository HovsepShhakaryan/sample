package com.vylo.main.responsemain.upload.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thumbnail(
    @SerializedName("cover_ext")
    val cover_ext: String? = null,
) : Parcelable