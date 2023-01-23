package com.vylo.main.profilefragment.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("category_global_id")
    val categoryGlobalId: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("response_to")
    val responseTo: String? = null,
    @SerializedName("status")
    val status: Int? = null
) : Parcelable
