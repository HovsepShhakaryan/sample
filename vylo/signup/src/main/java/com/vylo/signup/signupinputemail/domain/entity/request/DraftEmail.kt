package com.vylo.signup.signupinputemail.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DraftEmail(
    @SerializedName("email")
    val email: String? = null
) : Parcelable