package com.vylo.signup.signupdonotmissout.domain.usecase.impl

import com.vylo.signup.signupdonotmissout.data.repository.DoNotMissOutRepository
import com.vylo.signup.signupdonotmissout.domain.usecase.DoNotMissOutUseCase

class DoNotMissOutUseCaseImpl(
    private val repository: DoNotMissOutRepository
) : DoNotMissOutUseCase {
    override suspend fun getAutoFollow(page: Int?) = repository.getAutoFollow(page)

    override suspend fun addPublisher(id: String) = repository.addPublisher(id)

    override suspend fun deletePublisher(id: String) = repository.deletePublisher(id)
}