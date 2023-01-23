package com.vylo.common.domain.entity.responce

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tokens(
    @SerializedName("refresh")
    val refresh: String? = null,
    @SerializedName("access")
    val access: String? = null
) : Parcelable
