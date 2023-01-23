package com.vylo.androidapplication.domain.usecase.impl

import com.vylo.androidapplication.data.repository.impl.VersionRepositoryImpl
import com.vylo.androidapplication.domain.usecase.VersionUseCase

class VersionUseCaseImpl(
    private val versionRepository: VersionRepositoryImpl
) : VersionUseCase {

    override suspend fun getAppVersionName() =
        versionRepository.getAppVersionName()
}