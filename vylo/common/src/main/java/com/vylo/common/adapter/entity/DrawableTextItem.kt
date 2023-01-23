package com.vylo.common.adapter.entity

import android.graphics.drawable.Drawable

data class DrawableTextItem(
    val startText: String,
    val endText: String? = null,
    val startTextColor: Int? = null,
    val endTextColor: Int? = null,
    val startImage: Drawable? = null,
    val endImage: Drawable? = null,
    var isSwitch: Boolean? = null,
    val click: ((Boolean?) -> Unit)? = null
)