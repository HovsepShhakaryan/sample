package com.vylo.main.component.events.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ViewRequest(
    @SerializedName("news_global_id_list")
    val list: List<String>? = null
) : Parcelable