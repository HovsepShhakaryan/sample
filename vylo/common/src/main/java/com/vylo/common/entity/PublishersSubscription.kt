package com.vylo.common.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PublishersSubscription(
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
    var isFollow: Boolean = true
) : Parcelable {
    fun getUserName() = "@$username"
}