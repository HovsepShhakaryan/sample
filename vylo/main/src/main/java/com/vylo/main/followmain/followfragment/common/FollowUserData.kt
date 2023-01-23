package com.vylo.main.followmain.followfragment.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowUserData(
    val id: Long,
    val userName: String,
    val isApproved: Boolean,
    val followersCounter: Int,
    val followingCounter: Int,
    val isMyProfile: Boolean
) : Parcelable {
    fun getUserNameValue() = "@$userName"
}