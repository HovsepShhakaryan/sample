package com.vylo.main.responsemain.upload.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RespondData(
    val responseToGlobalId: String,
    val responseToTitle: String
) : Parcelable