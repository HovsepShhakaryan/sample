package com.vylo.common.data.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeneralError(
    @SerializedName("detail")
    val detail: String? = null
) : Parcelable
