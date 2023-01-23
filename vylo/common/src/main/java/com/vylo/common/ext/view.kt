package com.vylo.common.ext

import android.view.View
import android.widget.LinearLayout

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

fun View.toVisibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.toInvisible() {
    visibility = View.INVISIBLE
}

fun View.setLinearMarginTop(top: Int) {
    val params = this.layoutParams as LinearLayout.LayoutParams
    params.setMargins(
        params.leftMargin,
        resources.getDimensionPixelSize(top),
        params.rightMargin,
        params.bottomMargin
    )
    this.layoutParams = params
}