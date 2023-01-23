package com.vylo.auth.mainauthfragment.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Social(
    @SerializedName("data")
    val data: SocialItem? = null
) : Parcelable

@Parcelize
data class SocialItem(
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("id_token")
    val idToken: String? = null
) : Parcelable