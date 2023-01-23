package com.vylo.main.responsemain.upload.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColumnNews(
    @SerializedName("category_global_id")
    val categoryGlobalId: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("response_to")
    val responseTo: String? = null,
    @SerializedName("status")
    val status: Long? = null,
) : Parcelable
