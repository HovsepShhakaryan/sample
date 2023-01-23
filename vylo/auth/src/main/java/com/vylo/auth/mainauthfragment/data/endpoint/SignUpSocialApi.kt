package com.vylo.auth.mainauthfragment.data.endpoint

import com.vylo.auth.mainauthfragment.domain.entity.response.SocialData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SignUpSocialApi {

    companion object {
        private const val SIGN_IN_SOCIAL = "auth/social/{social}/"
    }

    @Multipart
    @POST(SIGN_IN_SOCIAL)
    suspend fun signInSocial(
        @Path("social") socialName: String,
        @Part body: MultipartBody.Part
    ): Response<SocialData>
}