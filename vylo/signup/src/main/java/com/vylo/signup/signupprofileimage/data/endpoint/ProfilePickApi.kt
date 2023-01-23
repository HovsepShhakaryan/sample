package com.vylo.signup.signupprofileimage.data.endpoint

import com.vylo.signup.signupprofileimage.domain.entity.response.ProfilePhotoItem
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfilePickApi {

    companion object {
        private const val PROFILE_IMAGE = "api/profile_image"
    }

    @Multipart
    @POST(PROFILE_IMAGE)
    suspend fun updateProfilePhoto(
        @Part body: List<MultipartBody.Part>
    ): Response<ProfilePhotoItem>
}