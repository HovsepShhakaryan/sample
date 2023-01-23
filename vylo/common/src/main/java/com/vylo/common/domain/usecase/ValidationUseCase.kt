package com.vylo.common.domain.usecase

interface ValidationUseCase {
    fun checkPasswordIsMatched(inputTypeFirstValue: String, inputTypeSecondValue: String): String
    fun checkConfirmPasswordIsEmpty(inputTypeValue: String): String
    fun checkEmailAddressIsEmpty(inputTypeValue: String): String
    fun checkEmailAddressIsValid(inputTypeValue: String): String
    fun checkPasswordIsEmpty(inputTypeValue: String): String
    fun checkUserNameIsEmpty(inputTypeValue: String): String
    fun checkFullNameIsEmpty(inputTypeValue: String): String
    fun checkPasswordIsValid(inputTypeValue: String): String
    fun checkUsernameIsValid(userName: String): String
    fun checkItemIsEmpty(input: String): String
}