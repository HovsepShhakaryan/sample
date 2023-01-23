package com.vylo.signup.signupprofileimage.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.signup.signupprofileimage.domain.entity.response.ProfilePhotoItem
import java.io.File

interface ProfilePickUseCase {
    suspend fun updateProfilePhoto(file: File): Resource<ProfilePhotoItem>
}