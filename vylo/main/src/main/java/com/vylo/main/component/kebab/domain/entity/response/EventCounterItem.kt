package com.vylo.main.component.kebab.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventCounterItem(
    @SerializedName("likes")
    val likes: Int? = null,
    @SerializedName("shares")
    val shares: Int? = null,
    @SerializedName("views")
    val views: Int? = null,
    @SerializedName("time_of_view")
    val timeOfView: Int? = null,
    @SerializedName("responses")
    val responses: Int? = null,
    @SerializedName("i_like_it")
    val iLikeIt: Boolean? = null,
    @SerializedName("i_saw_it")
    val iSawIt: Boolean? = null
) : Parcelable
