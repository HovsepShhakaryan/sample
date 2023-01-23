package com.vylo.common.api.apiservis

import android.content.Context
import com.vylo.common.api.Config
import com.vylo.common.api.NetworkConnectivityInterceptor
import com.vylo.common.api.TokenInterceptor
import com.vylo.common.util.SharedPreferenceData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiUploadVideo {

    private const val TIME_OUT: Long = 30000
    private lateinit var retrofit: Retrofit

    fun getInstance(context: Context): Retrofit {
        retrofit = getRetrofitObj(context)
        TokenInterceptor.getRetrofit(retrofit)
        return retrofit
    }

    private fun getRetrofitObj(context: Context): Retrofit {
        val builder = OkHttpClient().newBuilder()
        builder.addInterceptor(NetworkConnectivityInterceptor(context))
        builder.addInterceptor(TokenInterceptor(SharedPreferenceData(context)))
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS)
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        /*
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        builder.addInterceptor(interceptor)
         */

        val client = builder.build()

        return Retrofit.Builder().baseUrl(Config.BASE_URL_UPLOAD_VIDEO)
            .client(client).addConverterFactory(GsonConverterFactory.create()).build().also { retrofit = it }
    }

}