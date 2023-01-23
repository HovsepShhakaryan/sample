package com.vylo.main.profilefragment.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.vylo.common.entity.NewsItem
import com.vylo.main.homefragment.domain.entity.response.FeedItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileNewsData(
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<ProfileNewsItem>? = null,
) : Parcelable

@Parcelize
data class ProfileNewsItem(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("global_id")
    var globalId: String? = null,
    @SerializedName("is_user_news")
    var isUserNews: Boolean? = null,
    @SerializedName("is_approved")
    var isApproved: Int? = null,
    @SerializedName("status")
    var status: Int? = null,
    @SerializedName("category_id")
    var categoryId: String? = null,
    @SerializedName("category_name")
    var categoryName: String? = null,
    @SerializedName("publisher_id")
    var publisherId: String? = null,
    @SerializedName("publisher_name")
    var publisherName: String? = null,
    @SerializedName("publisher_image")
    var publisherImage: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("link")
    var link: String? = null,
    @SerializedName("published")
    var published: String? = null,
    @SerializedName("response_counter")
    var responseCounter: Int? = null,
    @SerializedName("response_to")
    var responseTo: ResponseTo? = null,
    @SerializedName("origin_global_id")
    var originGlobalId: String? = null,
    @SerializedName("thumbnail_type")
    var thumbnailType: String? = null,
    @SerializedName("thumbnail_url")
    var thumbnailUrl: String? = null,
    @SerializedName("content_type")
    var contentType: String? = null,
    @SerializedName("content_url")
    var contentUrl: String? = null,
    @SerializedName("content_duration")
    var contentDuration: Long? = null,
    @SerializedName("content_width")
    val contentWidth: Int? = null,
    @SerializedName("content_height")
    val contentHeight: Int? = null,
    @SerializedName("created_at")
    var createdAt: String? = null,
    @SerializedName("updated_at")
    var updatedAt: String? = null,
    @SerializedName("external_link")
    var externalLink: String? = null,
    @SerializedName("publisher_username")
    var username: String? = null,
    val isResponse: Boolean? = null
) : Parcelable {
    fun getUserName() = "@$username"
    fun checkResponse() = responseTo != null
}

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
    @SerializedName("content_url")
    val contentUrl: String? = null,
    @SerializedName("content_type")
    val contentType: String? = null,
    @SerializedName("publisher")
    val publisher: String? = null,
    @SerializedName("publisher_id")
    val publisherId: String? = null,
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

fun ProfileNewsItem.toNewsItem() = NewsItem(
    id = id,
    globalId = globalId,
    isUserNews = isUserNews,
    status = status,
    categoryId = categoryId,
    categoryName = categoryName,
    title = title,
    responseToTitle = responseTo?.title,
    contentUrl = contentUrl,
    externalLink = externalLink
)

fun FeedItem.toNewsItem() = NewsItem(
    id = id,
    globalId = globalId,
    isUserNews = isUserNews,
    status = 3,
    categoryId = categoryId,
    categoryName = categoryName,
    title = title,
    responseToGlobalId = responseTo?.globalId,
    responseToTitle = responseTo?.title,
    responseToCategoryId = responseTo?.category,
    responseToCategoryName = responseTo?.categoryName,
    contentUrl = contentUrl,
    externalLink = externalLink
)