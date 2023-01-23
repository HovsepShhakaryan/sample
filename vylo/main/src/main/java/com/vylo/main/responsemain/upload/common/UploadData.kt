package com.vylo.main.responsemain.upload.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadData(
    val id: String,
    val url: String,
    val title: String,
    val responseToTitle: String? = null,
    val categoryId: String,
    val categoryName: String,
    val status: Int? = null
) : Parcelable