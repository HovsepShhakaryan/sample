package com.vylo.main.profilefragment.data.repository.impl

import android.content.Context
import com.vylo.common.api.Resource
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.api.apiservis.ApiFeed
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.component.entity.ProfilePhotoItem
import com.vylo.main.profilefragment.data.endpoint.ProfileApi
import com.vylo.main.profilefragment.data.repository.ProfileRepository
import com.vylo.main.profilefragment.domain.entity.request.NewsUpdateRequest
import com.vylo.main.profilefragment.domain.entity.request.ProfileRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.java.KoinJavaComponent
import java.io.File

class ProfileRepositoryImpl(
    context: Context
) : BaseRepo(), ProfileRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(ProfileApi::class.java)

    private val apiFeed: ApiFeed by KoinJavaComponent.inject(ApiFeed::class.java)
    private val clientFeed = apiFeed.getInstance(context).create(ProfileApi::class.java)

    override suspend fun getProfile() = safeApiCall { client.getProfile() }
    override suspend fun getUserProfile(id: String) = safeApiCall { client.getUserProfile(id) }
    override suspend fun updateProfile(data: ProfileRequest) =
        safeApiCall { client.updateProfile(data) }

    override suspend fun updateProfilePhoto(file: File): Resource<ProfilePhotoItem> {
        val builder = MultipartBody.Builder()
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("profile_photo", file.name, requestFile)
        builder.addPart(body)
        file.deleteOnExit()

        return safeApiCall { client.updateProfilePhoto(builder.build().parts) }
    }

    override suspend fun getProfileNews(pageSize: Int?, cursor: String?) =
        safeApiCall { client.getProfileNews(pageSize, cursor) }

    override suspend fun getUserProfileNews(
        id: String,
        cursor: String?
    ) = safeApiCall { clientFeed.getUserProfileNews(id, cursor) }

    override suspend fun addPublisher(id: Long) = safeApiCall { client.addPublisher(id) }

    override suspend fun deletePublisher(id: Long) = safeApiCall { client.deletePublisher(id) }
    override suspend fun updateNews(id: String, data: NewsUpdateRequest) =
        safeApiCall { client.updateNews(id, data) }

    override suspend fun deleteNews(id: String) = safeApiCall { client.deleteNews(id) }
}