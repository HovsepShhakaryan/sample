package com.vylo.common.data.repository.error

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PasswordError(
    @SerializedName("new_password")
    val newPassword: List<String>? = null,
    @SerializedName("re_new_password")
    val reNewPassword: List<String>? = null,
    @SerializedName("current_password")
    val currentPassword: List<String>? = null,
    @SerializedName("non_field_errors")
    val nonFieldPassword: List<String>? = null
) : Parcelable