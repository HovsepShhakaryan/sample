package com.vylo.main.followmain.followfragment.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowingItem(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("is_source")
    val isSource: Boolean? = null,
    @SerializedName("is_approved")
    val isApproved: Boolean? = null,
    @SerializedName("profile_photo")
    val profilePhoto: String? = null,
    @SerializedName("im_following")
    val imFollowing: Boolean? = null
) : Parcelable {
    fun getUserName() = "@$username"
}