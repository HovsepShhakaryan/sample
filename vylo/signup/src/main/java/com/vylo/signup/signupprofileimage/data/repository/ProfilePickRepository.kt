package com.vylo.signup.signupprofileimage.data.repository

import com.vylo.common.api.Resource
import com.vylo.signup.signupprofileimage.domain.entity.response.ProfilePhotoItem
import java.io.File

interface ProfilePickRepository {
    suspend fun updateProfilePhoto(file: File): Resource<ProfilePhotoItem>
}