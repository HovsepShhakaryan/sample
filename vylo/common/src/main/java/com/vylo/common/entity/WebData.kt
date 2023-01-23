package com.vylo.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebData(
    val globalId: String? = null,
    val url: String? = null,
    val title: String? = null,
    val externalLink: String? = null,
    val categoryId: String? = null,
    val categoryName: String? = null
) : Parcelable