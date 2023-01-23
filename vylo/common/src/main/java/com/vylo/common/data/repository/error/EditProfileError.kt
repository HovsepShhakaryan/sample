package com.vylo.common.data.repository.error

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditProfileError(
    @SerializedName("name")
    val name: List<String>? = null,
    @SerializedName("username")
    val username: List<String>? = null,
    @SerializedName("website")
    val website: List<String>? = null,
    @SerializedName("bio")
    val bio: List<String>? = null
) : Parcelable
