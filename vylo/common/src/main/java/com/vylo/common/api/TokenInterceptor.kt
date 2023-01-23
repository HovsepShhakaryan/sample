package com.vylo.common.api

import com.vylo.common.data.endpoint.BaseCallApi
import com.vylo.common.domain.entity.request.GetToken
import com.vylo.common.domain.entity.responce.Tokens
import com.vylo.common.util.SharedPreferenceData
import kotlinx.coroutines.runBlocking
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit

class TokenInterceptor(
    private val sharedPreferenceData: SharedPreferenceData
) : Interceptor {

    private val unauthorised = 401

    companion object {
        private lateinit var retrofit: Retrofit

        fun getRetrofit(retrofit: Retrofit) {
            this.retrofit = retrofit
        }

        private const val AUTHORIZATION = "Authorization"
        private const val CSRF_TOKEN = "X-CSRFToken"
        private const val CONTENT_TYPE = "Content-Type"
        private const val ACCEPT = "accept"

        private const val BEARER = "Bearer"
        private const val ACCEPT_VALUE = "application/json"
    }

    private fun getHeaders(): Map<String, String>? {
        return if (sharedPreferenceData.retrieveToken != null && sharedPreferenceData.retrieveToken!!.isNotEmpty())
            sharedPreferenceData.retrieveToken?.let {
                mapOf(
                    AUTHORIZATION to "$BEARER $it",
                    CSRF_TOKEN to it,
                    ACCEPT to ACCEPT_VALUE
                )
            }
        else null
    }

    private fun Request.applyHeaders(headers: Headers): Request {
        return newBuilder().apply {
            removeHeader(AUTHORIZATION)
            removeHeader(CONTENT_TYPE)
            getHeaders()?.map {
                if (!(headers[HeaderExcluder.isToken] == "false" && (it.key == AUTHORIZATION || it.key == CSRF_TOKEN))) {
                    addHeader(it.key, it.value)
                }
            }
        }.build()
    }

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val headers = chain.request().headers
        val request: Request = chain.request().applyHeaders(headers)
        var newRequest: Request? = null

        val response = chain.proceed(request = request)
        if (!response.isSuccessful && response.code == unauthorised) {
            response.close()

            // API service to get refresh token
            val tokenService = retrofit.create(BaseCallApi::class.java)

            val apiToken: retrofit2.Response<Tokens> = tokenService.refreshTokenCall(
                GetToken(
                    sharedPreferenceData.retrieveRefreshToken
                )
            )

            if (apiToken.body() != null) {
                sharedPreferenceData.saveToken(apiToken.body()!!.access.toString())
                sharedPreferenceData.saveRefreshToken(apiToken.body()!!.refresh.toString())
            }

            sharedPreferenceData.retrieveToken?.let {
                newRequest = request.applyHeaders(headers)
            }


            if (newRequest != null) chain.proceed(request = newRequest!!)
            else chain.proceed(request = request)

        } else {
            response
        }
    }
}