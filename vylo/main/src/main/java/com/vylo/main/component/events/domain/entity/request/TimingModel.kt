package com.vylo.main.component.events.domain.entity.request

import com.vylo.main.homefragment.domain.entity.response.FeedItem

data class TimingModel(
    val position: Int? = null,
    val addedTime: Int? = null,
    val destroyedTime: Int? = null,
    val item: FeedItem? = null
)

