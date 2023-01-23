package com.vylo.common.data.repository.error

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResetPassError(
    @SerializedName("non_field_errors")
    val nonFieldErrors: List<String>? = null
): Parcelable
