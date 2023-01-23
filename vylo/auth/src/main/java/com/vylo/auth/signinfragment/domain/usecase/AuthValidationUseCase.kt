package com.vylo.auth.signinfragment.domain.usecase

import com.vylo.auth.signinfragment.domain.entity.request.SignIn

interface AuthValidationUseCase {
    fun requestSignInModel(): SignIn
    fun checkEmailAddressIsEmpty(inputTypeValue: String): String
    fun checkPasswordIsEmpty(inputTypeValue: String): String
    fun checkEmailAddressIsValid(inputTypeValue: String): String
    fun checkPasswordIsValid(inputTypeValue: String): String
}