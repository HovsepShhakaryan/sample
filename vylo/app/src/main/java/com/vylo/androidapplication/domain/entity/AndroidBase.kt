package com.vylo.androidapplication.domain.entity

import com.google.gson.annotations.SerializedName

data class AndroidBase(
    @SerializedName("Android")
    val android: Android? = Android()
)

data class Android (
    @SerializedName("app_version")
    val appVersion: String? = null
)
