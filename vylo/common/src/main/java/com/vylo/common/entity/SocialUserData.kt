package com.vylo.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SocialUserData(
    val userName: String? = null,
    val userPhoto: String? = null,
) : Parcelable
