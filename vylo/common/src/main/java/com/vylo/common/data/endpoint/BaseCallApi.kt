package com.vylo.common.data.endpoint

import com.vylo.common.domain.entity.request.GetToken
import com.vylo.common.domain.entity.responce.Tokens
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BaseCallApi {

    @POST(REFRESH_TOKEN_CALL)
    suspend fun refreshTokenCall(@Body getToken: GetToken): Response<Tokens>
}