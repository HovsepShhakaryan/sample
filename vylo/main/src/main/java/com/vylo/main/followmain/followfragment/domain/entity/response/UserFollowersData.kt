package com.vylo.main.followmain.followfragment.domain.entity.response

import com.google.gson.annotations.SerializedName

data class UserFollowersData(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("profile_photo")
    val profile_photo: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("website")
    val website: String? = null,
    @SerializedName("bio")
    val bio: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("categories_subscription")
    val categories_subscription: List<String>? = null,
    @SerializedName("publishers_subscription")
    val publishers_subscription: List<UserFollowersItem>? = null,
    @SerializedName("post_counter")
    val post_counter: Int? = null,
    @SerializedName("followers_counter")
    val followers_counter: Int? = null,
    @SerializedName("following_counter")
    val following_counter: Int? = null,
    @SerializedName("birthday_date")
    val birthday_date: String? = null,
)