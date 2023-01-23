package com.vylo.main.responsemain.upload.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColumnNewsData(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("category")
    val category: Long? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("response_to")
    val responseTo: String? = null,
    @SerializedName("status")
    val status: Long? = null,
) : Parcelable
