package com.vylo.common.domain.usecase.impl

import com.vylo.common.data.Mapper
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase

class GeneralErrorMapperUseCaseImpl(
    private val mapper: Mapper
) : GeneralErrorMapperUseCase {

    override fun fromApiErrorToSignInErrorFromArray(baseResponse: Any?) =
        mapper.fromApiErrorToSignInErrorFromArrayCode(baseResponse)

    override fun getApiErrorMessageArray(baseResponse: Any?) =
        mapper.fromApiErrorToSignInErrorFromArray(baseResponse)

    override fun fromApiErrorToStringEmail(baseResponse: Any?) =
        mapper.fromApiErrorToStringEmail(baseResponse)

    override fun fromApiErrorToStringCode(baseResponse: Any?) =
        mapper.fromApiErrorToStringCode(baseResponse)

    override fun getApiErrorMessage(baseResponse: Any?) =
        mapper.fromApiErrorToSignInError(baseResponse)

    override fun getApiEditProfileErrorMessage(baseResponse: Any?) =
        mapper.fromApiErrorToEditProfileError(baseResponse)

    override fun getApiPersonalInformationErrorMessage(baseResponse: Any?) =
        mapper.fromApiErrorToPersonalInformationError(baseResponse)

    override fun getApiReportErrorMessage(baseResponse: Any?) =
        mapper.fromApiErrorToReportError(baseResponse)

    override fun getApiPasswordErrorMessage(baseResponse: Any?) =
        mapper.fromApiErrorToPasswordError(baseResponse)

    override fun getApiUpdateNewsErrorMessage(baseResponse: Any?) =
        mapper.fromApiErrorToUpdateNewsError(baseResponse)
}