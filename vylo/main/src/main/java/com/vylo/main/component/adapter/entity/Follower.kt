package com.vylo.main.component.adapter.entity

import com.vylo.common.adapter.entity.CategoryInfo
import com.vylo.common.adapter.entity.UserInfo

sealed class Follower {
    data class FollowerUser(
        val userId: Long,
        val userInfo: UserInfo
    ) : Follower()

    data class FollowerCategory(
        val userId: Long,
        val userInfo: CategoryInfo
    ) : Follower()
}
