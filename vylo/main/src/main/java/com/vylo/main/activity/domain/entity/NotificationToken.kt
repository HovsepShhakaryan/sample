package com.vylo.main.activity.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationToken(
    val name: String? = null,
    val registration_id: String? = null,
    val device_id: String? = null,
    val active: Boolean = true,
    val type: String = "android"
) : Parcelable
