package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PasswordRequest(
    @SerializedName("new_password")
    val newPassword: String? = null,
    @SerializedName("re_new_password")
    val reNewPassword: String? = null,
    @SerializedName("current_password")
    val currentPassword: String? = null
) : Parcelable
