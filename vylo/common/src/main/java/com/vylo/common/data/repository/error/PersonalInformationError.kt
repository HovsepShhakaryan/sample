package com.vylo.common.data.repository.error

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonalInformationError(
    @SerializedName("email")
    val email: List<String>? = null,
    @SerializedName("phone")
    val phone: List<String>? = null,
    @SerializedName("gender")
    val gender: List<String>? = null,
    @SerializedName("birthday_date")
    val birthdayDate: List<String>? = null
) : Parcelable
