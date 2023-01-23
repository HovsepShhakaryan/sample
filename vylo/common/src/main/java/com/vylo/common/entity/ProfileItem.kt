package com.vylo.common.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileItem(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("global_id")
    var globalId: String? = null,
    @SerializedName("username")
    var username: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("profile_photo")
    var profilePhoto: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("website")
    var website: String? = null,
    @SerializedName("bio")
    var bio: String? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("categories_subscription")
    var categoriesSubscription: List<CategoriesSubscription>? = null,
    @SerializedName("publishers_subscription")
    var publishersSubscription: List<PublishersSubscription>? = null,
    @SerializedName("post_counter")
    var postCounter: Int? = null,
    @SerializedName("followers_counter")
    var followersCounter: Int? = null,
    @SerializedName("following_counter")
    var followingCounter: Int? = null,
    @SerializedName("birthday_date")
    var birthdayDate: String? = null
) : Parcelable