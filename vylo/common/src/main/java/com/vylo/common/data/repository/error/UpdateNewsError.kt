package com.vylo.common.data.repository.error

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateNewsError(
    @SerializedName("detail")
    val detail: String? = null,
    @SerializedName("id")
    val id: List<Int>? = null,
    @SerializedName("category_global_id")
    val categoryGlobalId: List<String>? = null,
    @SerializedName("title")
    val title: List<String>? = null,
    @SerializedName("global_id")
    val globalId: List<String>? = null,
    @SerializedName("link")
    val link: List<String>? = null,
    @SerializedName("author")
    val author: List<String>? = null,
    @SerializedName("response_to")
    val responseTo: List<String>? = null,
    @SerializedName("status")
    val status: List<Int>? = null
) : Parcelable
