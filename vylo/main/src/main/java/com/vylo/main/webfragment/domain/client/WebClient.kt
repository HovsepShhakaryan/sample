package com.vylo.main.webfragment.domain.client

import android.webkit.WebView
import android.webkit.WebViewClient

class WebClient(
    private val hideProgress: () -> Unit
) : WebViewClient() {

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        super.onPageCommitVisible(view, url)
        hideProgress()
    }
}