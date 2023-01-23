package com.vylo.signup.signupprofileimage.domain.usecase.impl

import com.vylo.signup.signupprofileimage.data.repository.ProfilePickRepository
import com.vylo.signup.signupprofileimage.domain.usecase.ProfilePickUseCase
import java.io.File

class ProfilePickUseCaseImpl(
    private val profilePickRepository: ProfilePickRepository
) : ProfilePickUseCase {

    override suspend fun updateProfilePhoto(file: File) = profilePickRepository.updateProfilePhoto(file)
}