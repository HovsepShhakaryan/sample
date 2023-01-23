package com.vylo.main.videofragment.common

import android.os.Parcelable
import com.vylo.common.util.enums.VideoType
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoData(
    val globalId: String,
    val contentUrl: String,
    val publisherId: String,
    val publisherImage: String,
    val publisherName: String,
    val title: String,
    val externalLink: String,

    val responseToGlobalId: String? = null,
    val responseToContentUrl: String? = null,
    val responseToTitle: String? = null,
    val responseToLink: String? = null,
    val responseToExternalLink: String? = null,
    val responseCounter: Long? = null,
    val responseToPublisherId: String? = null,
    val responseToPublisherImage: String? = null,
    val responseToPublisherName: String? = null,
    val responseToImage: String? = null,

    val videoType: VideoType
) : Parcelable