package com.vylo.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RespondData(
    val responseToGlobalId: String,
    val responseToTitle: String,
    val responseCategoryId: String,
    val responseCategoryName: String
) : Parcelable