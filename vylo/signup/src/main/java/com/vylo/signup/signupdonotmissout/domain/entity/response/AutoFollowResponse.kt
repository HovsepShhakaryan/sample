package com.vylo.signup.signupdonotmissout.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AutoFollowData(
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<AutoFollowItem>? = null,
) : Parcelable

@Parcelize
data class AutoFollowItem(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("profile_photo")
    val profilePhoto: String? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("is_approved")
    val isApproved: Boolean? = null,
    @SerializedName("description")
    val description: String? = null
) : Parcelable {
    fun getUserName() = "@$username"
}