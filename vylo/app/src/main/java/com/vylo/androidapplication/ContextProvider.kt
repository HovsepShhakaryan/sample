package com.vylo.androidapplication

import android.content.Context

interface ContextProvider {
    fun getActivityContext(): Context?
}
