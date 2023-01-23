package com.vylo.main.profilefragment.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileRequest(
    @SerializedName("username")
    val username: String? = null,
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
) : Parcelable
