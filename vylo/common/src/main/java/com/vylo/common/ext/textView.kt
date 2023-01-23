package com.vylo.common.ext

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.createLinks(linkId: Int, colorId: Int = currentTextColor, block: () -> Unit) {
    val span = SpannableString(text)
    val link = context.getString(linkId)
    val firstIndex = text.indexOf(link)
    val lastIndex = firstIndex + link.length
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            block.invoke()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = colorId
            ds.isUnderlineText = false
        }
    }
    val bold = StyleSpan(Typeface.BOLD)
    span.setSpan(clickableSpan, firstIndex, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    span.setSpan(bold, firstIndex, lastIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = span
    movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.drawableEnd(drawableRes: Int, paddingRes: Int? = null) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0)
    this.setDrawablePadding(paddingRes)
}

fun TextView.drawableStart(drawableRes: Int, paddingRes: Int? = null) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0)
    val dr = ContextCompat.getDrawable(context, drawableRes)
    this.setDrawablePadding(paddingRes)
}

fun TextView.drawableTop(drawableRes: Int, paddingRes: Int? = null) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, drawableRes, 0, 0)
    val dr = ContextCompat.getDrawable(context, drawableRes)
    this.setDrawablePadding(paddingRes)
}

fun TextView.drawableBottom(drawableRes: Int, paddingRes: Int? = null) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawableRes)
    val dr = ContextCompat.getDrawable(context, drawableRes)
    this.setDrawablePadding(paddingRes)
}

private fun TextView.setDrawablePadding(paddingRes: Int?) {
    if (paddingRes != null) {
        val padding = context.resources.getDimensionPixelSize(paddingRes)
        this.compoundDrawablePadding = padding
    }
}

fun TextView.setTextOrGone(text: String?) {
    if (text.isNullOrEmpty()) {
        this.toGone()
    } else {
        this.text = text
        this.toVisible()
    }
}