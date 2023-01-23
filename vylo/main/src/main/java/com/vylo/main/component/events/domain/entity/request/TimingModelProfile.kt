package com.vylo.main.component.events.domain.entity.request

import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsItem

data class TimingModelProfile(
    val position: Int? = null,
    val addedTime: Int? = null,
    val destroyedTime: Int? = null,
    val item: ProfileNewsItem? = null
)