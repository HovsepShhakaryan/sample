package com.vylo.main.component.events.domain.entity.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VyloView(
    val percent: String? = null,
    val globalId: String? = null
) : Parcelable
