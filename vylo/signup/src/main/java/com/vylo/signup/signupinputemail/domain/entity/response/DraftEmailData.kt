package com.vylo.signup.signupinputemail.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DraftEmailData(
    @SerializedName("is_valid")
    val isValid: Boolean? = null
) : Parcelable