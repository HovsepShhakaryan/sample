package com.vylo.signup.signupwebview.domain

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

class WebClient(
    private val showProgress: () -> Unit,
    private val hideProgress: () -> Unit
) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        showProgress()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        hideProgress()
    }
}