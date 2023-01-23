package com.vylo.main.component.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileInfo(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("profile_photo")
    val profilePhoto: String? = null,
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
    @SerializedName("birthday_date")
    val birthdayDate: String? = null
) : Parcelable {
    fun getUserName() = "@$username"
}