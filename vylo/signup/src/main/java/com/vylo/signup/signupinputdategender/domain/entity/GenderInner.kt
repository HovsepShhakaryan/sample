package com.vylo.signup.signupinputdategender.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenderInner(
    val genderName: String? = null,
    var isChosen: Boolean? = null
) : Parcelable
