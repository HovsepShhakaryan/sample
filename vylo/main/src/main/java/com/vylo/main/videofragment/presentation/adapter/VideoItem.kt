package com.vylo.main.videofragment.presentation.adapter

import com.vylo.main.homefragment.domain.entity.response.FeedItem

data class VideoItem(
    val videoItem: FeedItem,
    var isLike: Boolean,
    var isFollowing: Boolean,
    var publisherId: Long
)
