package com.vylo.signup.signupprofileimage.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfilePhotoItem(
    @SerializedName("profile_photo")
    val profilePhoto: String? = null
) : Parcelable
