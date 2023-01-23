package com.vylo.main.insightfulfragment.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InsightfulData(
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<InsightfulItem>?
) : Parcelable

@Parcelize
data class InsightfulItem(
    @SerializedName("type_event")
    val typeEvent: Int? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("publisher_username")
    val publisherUsername: String? = null,
    @SerializedName("publisher_global_id")
    val publisherGlobalId: String? = null,
    @SerializedName("publisher_image")
    val publisherImage: String? = null,
    @SerializedName("origin_global_id")
    val originGlobalId: String? = null,
    @SerializedName("origin_title")
    val originTitle: String? = null,
    @SerializedName("origin_publisher_username")
    val originPublisherUsername: String? = null,
    @SerializedName("origin_publisher_global_id")
    val originPublisherGlobalId: String? = null,
    @SerializedName("origin_thumbnail_url")
    val originThumbnailUrl: String? = null,
    @SerializedName("published")
    val published: String? = null
) : Parcelable