package com.vylo.common.util

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vylo.common.R
import com.vylo.common.util.enums.BaseUrlType

class ThrowStartScreen(
    private val context: Context
) {

    private val authentication = context.resources.getString(R.string.label_return_start_screen)

    fun throwStartScreen() {
        val intent = Intent(authentication)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}