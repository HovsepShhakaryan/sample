package com.vylo.common.ext

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan

fun SpannableStringBuilder.appendWithBold(text: String?) {
    append(text)
    setSpan(
        StyleSpan(Typeface.BOLD),
        length - text?.length.orZero(),
        length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}