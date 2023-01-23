package com.vylo.auth.signinfragment.domain.usecase.impl

import com.vylo.auth.signinfragment.domain.entity.request.SignIn
import com.vylo.auth.signinfragment.domain.usecase.AuthValidationUseCase
import com.vylo.common.domain.usecase.impl.ValidationUseCaseImpl

class AuthValidationUseCaseImpl(
    private val validationUseCaseImpl: ValidationUseCaseImpl
) : AuthValidationUseCase {

    override fun requestSignInModel() = SignIn(
        validationUseCaseImpl.email,
        validationUseCaseImpl.password
    )

    override fun checkEmailAddressIsEmpty(inputTypeValue: String) =
        validationUseCaseImpl.checkEmailAddressIsEmpty(inputTypeValue)

    override fun checkPasswordIsEmpty(inputTypeValue: String) =
        validationUseCaseImpl.checkPasswordIsEmpty(inputTypeValue)

    override fun checkEmailAddressIsValid(inputTypeValue: String) =
        validationUseCaseImpl.checkEmailAddressIsValid(inputTypeValue)

    override fun checkPasswordIsValid(inputTypeValue: String) =
        validationUseCaseImpl.checkPasswordIsValid(inputTypeValue)
}