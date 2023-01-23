package com.vylo.main.component.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.vylo.common.domain.localentity.entity.PublishersEntity
import com.vylo.common.ext.orFalse
import com.vylo.common.ext.orZero
import kotlinx.parcelize.Parcelize

@Parcelize
data class PublishersSubscriptionData(
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<PublishersSubscription>? = null
) : Parcelable

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
    var isFollow: Boolean = true,
    @SerializedName("im_following")
    val imFollowing: Boolean? = null
) : Parcelable {
    fun getUserName() = "@$username"
}

fun PublishersSubscription.toPublishersEntity() = PublishersEntity(
    id = id.orZero(),
    globalId = globalId.orEmpty(),
    userName = username.orEmpty(),
    name = name.orEmpty(),
    isSource = isSource.orFalse(),
    isApproved = isApproved.orFalse(),
    profilePhoto = profilePhoto.orEmpty()
)