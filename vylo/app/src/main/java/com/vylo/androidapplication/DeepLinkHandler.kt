package com.vylo.androidapplication

import android.content.Intent

class DeepLinkHandler {

    fun getDeepLinkId(intent: Intent): String? {
        val data = intent.data
        if (data != null)
            return data.toString()
        return null
    }
}