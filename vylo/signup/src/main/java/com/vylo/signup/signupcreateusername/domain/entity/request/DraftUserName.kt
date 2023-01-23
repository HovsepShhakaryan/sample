package com.vylo.signup.signupcreateusername.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DraftUserName(
    @SerializedName("username")
    val username: String? = null
) : Parcelable
