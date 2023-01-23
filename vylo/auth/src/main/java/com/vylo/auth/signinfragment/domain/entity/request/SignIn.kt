package com.vylo.auth.signinfragment.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignIn(
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("password")
    val password: String? = null
) : Parcelable
