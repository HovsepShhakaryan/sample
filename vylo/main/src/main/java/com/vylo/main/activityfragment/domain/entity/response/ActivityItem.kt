package com.vylo.main.activityfragment.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActivityData(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<ActivityItem>? = null,
) : Parcelable

@Parcelize
data class ActivityItem(
    @SerializedName("type_activity")
    val typeActivity: Int? = null,
    @SerializedName("publisher_global_id")
    val publisherGlobalId: String? = null,
    @SerializedName("publisher_username")
    val publisherUsername: String? = null,
    @SerializedName("publisher_is_approved")
    val publisherIsApproved: Boolean? = null,
    @SerializedName("publisher_image")
    val publisherImage: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("news_global_id")
    val newsGlobalId: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("thumbnail_type")
    val thumbnailType: String? = null,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null,
    @SerializedName("origin_global_id")
    val originGlobalId: String? = null,
    @SerializedName("origin_title")
    val originTitle: String? = null,
    @SerializedName("origin_thumbnail_url")
    val originThumbnailUrl: String? = null,
    @SerializedName("origin_publisher_username")
    val originPublisherUsername: String? = null,
    @SerializedName("origin_publisher_global_id")
    val originPublisherGlobalId: String? = null,
    @SerializedName("origin_publisher_is_approved")
    val originPublisherIsApproved: Boolean? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
) : Parcelable {
    fun getPublisherUserName() = "@$publisherUsername"
    fun getOriginalPublisherUserName() = "@$originPublisherUsername"
}