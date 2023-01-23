package com.vylo.main.followmain.followfragment.domain.entity.response

import com.google.gson.annotations.SerializedName

data class UserFollowersItem(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("global_id")
    val global_id: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("is_source")
    val is_source: Boolean? = null,
    @SerializedName("is_approved")
    val is_approved: Boolean? = null,
    @SerializedName("profile_photo")
    val profile_photo: String? = null
)
