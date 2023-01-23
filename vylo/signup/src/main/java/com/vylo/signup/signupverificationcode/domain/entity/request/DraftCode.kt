package com.vylo.signup.signupverificationcode.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DraftCode(
    @SerializedName("email")
    val email: String? = null
) : Parcelable
