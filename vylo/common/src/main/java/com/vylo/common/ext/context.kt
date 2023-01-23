package com.vylo.common.ext

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ShareCompat
import com.vylo.common.R

fun Context.hideKeyboard(view: View) {
    val manager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.toClipboard(data: String) {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", data)
    clipboardManager.setPrimaryClip(clipData)
}

fun Context.shareLink(link: String) {
    ShareCompat.IntentBuilder(this)
        .setType("text/plain")
        .setChooserTitle(resources.getString(R.string.label_share_link))
        .setText(link)
        .startChooser()
}