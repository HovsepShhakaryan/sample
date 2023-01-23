package com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileData(
    @SerializedName("count")
    val count: Long? = null,
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<ProfileItem>? = null
) : Parcelable

@Parcelize
data class ProfileItem(
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
    var isFollow: Boolean = false
) : Parcelable {
    fun getUserNameValue() = "@$username"
}