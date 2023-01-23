package com.vylo.common.adapter.entity

import android.graphics.drawable.Drawable

data class KebabOption(
    val option: String,
    val drawImage: Drawable? = null,
    val imagesRes: List<Int>? = null,
    val imagePaddingRes: Int? = null,
    val color: Int? = null,
    val click: (() -> Unit)? = null,
    val clickWithParam: ((Any?) -> Unit)? = null,
    var isActive: Boolean = false
)