package com.vylo.main.following.data.endpoint

import com.vylo.common.entity.ProfileItem
import retrofit2.Response
import retrofit2.http.GET

interface PublishersApi {

    @GET(PROFILE)
    suspend fun getProfile(): Response<ProfileItem>

    companion object {
        private const val PROFILE = "api/profile"
    }
}