package com.vylo.resetpassword.forgotpassemail.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SendEmail(
    @SerializedName("email")
    val email: String? = null
) : Parcelable
