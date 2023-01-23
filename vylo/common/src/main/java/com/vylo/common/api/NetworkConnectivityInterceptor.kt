package com.vylo.common.api

import android.content.Context
import android.os.Handler
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import android.os.Looper
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vylo.common.R
import com.vylo.common.util.NetworkConnection

class NetworkConnectivityInterceptor(private val context: Context) : Interceptor {

    private var isNetworkActive = false

    override fun intercept(chain: Interceptor.Chain): Response {
        isNetworkActive = NetworkConnection(context).isOnline()
        return if (!isNetworkActive) {
            throw NoConnectivityException(context)
        } else {
            chain.proceed(chain.request())
        }
    }

}

class NoConnectivityException(private val context: Context) : IOException() {

    override val message: String
        get() = context.getString(R.string.label_no_connection)

    init {
        postToBroadcastShowNetworkConnectionProblem(context, message)
    }

    private fun postToBroadcastShowNetworkConnectionProblem(context: Context?, msg: String?) {
        if (context != null && msg != null) {
            Handler(Looper.getMainLooper()).post {
                val intent = Intent(msg)
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }
    }

}