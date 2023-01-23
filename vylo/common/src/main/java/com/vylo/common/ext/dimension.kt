package com.vylo.common.ext

import android.content.Context

fun Float.pxToSp(context: Context): Float {
    val density = context.resources.displayMetrics.scaledDensity
    return this / density
}

fun Int.pxToDp(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this / density).toInt()
}

fun Int.dpToPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this * density).toInt()
}