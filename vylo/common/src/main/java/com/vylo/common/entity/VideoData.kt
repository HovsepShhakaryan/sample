package com.vylo.common.entity

import android.os.Parcelable
import com.vylo.common.util.enums.VideoType
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoData(
    val globalId: String,
    val videoType: VideoType? = null
) : Parcelable