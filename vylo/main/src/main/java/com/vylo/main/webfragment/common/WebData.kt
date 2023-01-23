package com.vylo.main.webfragment.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebData(
    val globalId: String,
    val url: String,
    val responsesCount: Long? = null
) : Parcelable