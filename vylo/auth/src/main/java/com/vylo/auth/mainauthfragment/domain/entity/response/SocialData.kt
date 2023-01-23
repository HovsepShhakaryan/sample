package com.vylo.auth.mainauthfragment.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SocialData(
    @SerializedName("is_new")
    val isNew: Boolean? = null,
    @SerializedName("access")
    val access: String? = null,
    @SerializedName("refresh")
    val refresh: String? = null,
    @SerializedName("partial_token")
    val partialToken: String? = null
) : Parcelable
