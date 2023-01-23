package com.vylo.main.homefragment.domain.mappers

import com.vylo.main.homefragment.domain.entity.response.FeedItem
import com.vylo.main.videofragment.presentation.adapter.VideoItem

fun List<FeedItem>.toVideoItemList(): List<VideoItem> = this.map { it.toVideoItem() }

fun FeedItem.toVideoItem() = VideoItem(
    videoItem = this,
    isLike = false,
    isFollowing = false,
    publisherId = 0
)