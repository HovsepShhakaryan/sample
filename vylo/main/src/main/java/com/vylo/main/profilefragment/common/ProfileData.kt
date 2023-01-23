package com.vylo.main.profilefragment.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileData(
    val id: String
) : Parcelable