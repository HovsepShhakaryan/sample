package com.vylo.common.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetToken(
    @SerializedName("refresh")
    val refresh: String? = null
) : Parcelable
