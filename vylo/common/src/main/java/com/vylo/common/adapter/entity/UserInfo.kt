package com.vylo.common.adapter.entity
data class UserInfo(
    val id: Long,
    val globalId: String?,
    val reporterImage: String?,
    val name: String,
    val subName: String = "",
    val isVerified: Boolean = false,
    val isLabelFollowing: Boolean = false,
    val isButtonFollowing: Boolean = false,
    var isFollower: Boolean = false
)