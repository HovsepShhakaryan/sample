package com.vylo.globalsearch.searchnewsstand.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchNewsstandData(
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<SearchNewsstandItem>? = null,
) : Parcelable

@Parcelize
data class SearchNewsstandItem(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("is_user_news")
    val isUserNews: Boolean? = null,
    @SerializedName("category_id")
    val categoryId: String? = null,
    @SerializedName("category_name")
    val categoryName: String? = null,
    @SerializedName("publisher_id")
    val publisherId: String? = null,
    @SerializedName("publisher_name")
    val publisherName: String? = null,
    @SerializedName("publisher_image")
    val publisherImage: String? = null,
    @SerializedName("publisher_is_approved")
    val publisherIsApproved: Boolean? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("external_link")
    val externalLink: String? = null,
    @SerializedName("thumbnail_type")
    val thumbnailType: String? = null,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null,
    @SerializedName("content_type")
    val contentType: String? = null,
    @SerializedName("content_url")
    val contentUrl: String? = null,
    @SerializedName("published")
    val published: String? = null,
    @SerializedName("response_counter")
    val responseCounter: Long? = null,
    @SerializedName("response_to")
    val responseTo: ResponseTo? = null,
    @SerializedName("origin_global_id")
    val originGlobalId: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
) : Parcelable

@Parcelize
data class ResponseTo(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("published")
    val published: String? = null,
    @SerializedName("publisher")
    val publisher: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("media_links")
    val mediaLinks: List<Long>? = null,
    @SerializedName("category_name")
    val categoryName: String? = null,
    @SerializedName("publisher_name")
    val publisherName: String? = null,
    @SerializedName("publisher_image")
    val publisherImage: String? = null,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null
) : Parcelable