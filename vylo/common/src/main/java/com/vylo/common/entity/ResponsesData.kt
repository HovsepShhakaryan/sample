package com.vylo.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponsesData(
    val globalId: String
) : Parcelable