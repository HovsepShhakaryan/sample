package com.vylo.resetpassword.forgotpassemail.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SendEmailData(
    @SerializedName("token")
    val token: String? = null
) : Parcelable
