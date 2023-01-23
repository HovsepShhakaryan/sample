package com.vylo.common.domain.usecase

import com.vylo.common.data.repository.error.*

interface GeneralErrorMapperUseCase {
    fun fromApiErrorToSignInErrorFromArray(baseResponse: Any?): ResetPassError?
    fun getApiErrorMessageArray(baseResponse: Any?): String?
    fun fromApiErrorToStringEmail(baseResponse: Any?): String?
    fun fromApiErrorToStringCode(baseResponse: Any?): String?
    fun getApiErrorMessage(baseResponse: Any?): GeneralError?
    fun getApiEditProfileErrorMessage(baseResponse: Any?): EditProfileError?
    fun getApiPersonalInformationErrorMessage(baseResponse: Any?): PersonalInformationError?
    fun getApiReportErrorMessage(baseResponse: Any?): ReportError?
    fun getApiPasswordErrorMessage(baseResponse: Any?): PasswordError?
    fun getApiUpdateNewsErrorMessage(baseResponse: Any?): UpdateNewsError?
}