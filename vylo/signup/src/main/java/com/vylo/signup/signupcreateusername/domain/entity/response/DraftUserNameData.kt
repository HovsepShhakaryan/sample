package com.vylo.signup.signupcreateusername.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DraftUserNameData(
    @SerializedName("is_valid")
    val isValid: Boolean? = null
) : Parcelable
