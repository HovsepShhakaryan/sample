package com.vylo.signup.signupprofileimage.data.repository.impl

import android.content.Context
import com.vylo.common.api.Resource
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.signup.signupprofileimage.data.endpoint.ProfilePickApi
import com.vylo.signup.signupprofileimage.data.repository.ProfilePickRepository
import com.vylo.signup.signupprofileimage.domain.entity.response.ProfilePhotoItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.java.KoinJavaComponent
import java.io.File

class ProfilePickRepositoryImpl(
    context: Context
) : BaseRepo(), ProfilePickRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(ProfilePickApi::class.java)

    override suspend fun updateProfilePhoto(file: File): Resource<ProfilePhotoItem> {
        val builder = MultipartBody.Builder()
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("profile_photo", file.name, requestFile)
        builder.addPart(body)
        file.deleteOnExit()

        return safeApiCall { client.updateProfilePhoto(builder.build().parts) }
    }
}