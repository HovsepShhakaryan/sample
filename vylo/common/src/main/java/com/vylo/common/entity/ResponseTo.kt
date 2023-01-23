package com.vylo.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseTo(
    val globalId: String? = null,
    val contentUrl: String? = null,
    val publisherId: String? = null,
    val publisherImage: String? = null,
    val publisherName: String? = null,
    val title: String? = null,
    val link: String? = null,
    val externalLink: String? = null,
    val thumbnailUrl: String? = null
) : Parcelable
