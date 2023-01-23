package com.vylo.resetpassword.forgotpasscode.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfirmPass(
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("new_password")
    val newPassword: String? = null,
    @SerializedName("re_new_password")
    val reNewPassword: String? = null
) : Parcelable
