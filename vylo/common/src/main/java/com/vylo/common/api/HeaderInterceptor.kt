package com.vylo.common.api

import android.content.Context
import com.vylo.common.R
import com.vylo.common.util.SharedPreferenceData
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val sharedPreferenceData: SharedPreferenceData,
    private val context: Context
) : Interceptor {

//    private val authentication = context.resources.getString(R.string.label_unauthorised)
    private val unauthorised = 401

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        sharedPreferenceData.retrieveToken?.let {
            requestBuilder.addHeader("X-CSRFToken", it)
            requestBuilder.addHeader("Content-Type", "application/json")
            requestBuilder.addHeader("accept", "application/json")
        }

        val response = chain.proceed(requestBuilder.build())
        if (response.code == unauthorised) {

        }

        return response
    }



}
