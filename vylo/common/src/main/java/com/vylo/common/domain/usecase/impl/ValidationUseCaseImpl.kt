package com.vylo.common.domain.usecase.impl

import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.common.validation.Validation

class ValidationUseCaseImpl(
    private val validation: Validation
) : ValidationUseCase {

    var password: String? = null
    var userName: String? = null
    var fullName: String? = null
    var email: String? = null

    override fun checkPasswordIsMatched(inputTypeFirstValue: String, inputTypeSecondValue: String) =
        validation.isFieldsMatched(inputTypeFirstValue, inputTypeSecondValue)

    override fun checkConfirmPasswordIsEmpty(inputTypeValue: String) = validation.isInputEmpty(inputTypeValue)

    override fun checkEmailAddressIsValid(inputTypeValue: String) = validation.isValidEmailWithText(inputTypeValue)

    override fun checkPasswordIsValid(inputTypeValue: String) = validation.isValidPasswordWithText(inputTypeValue)

    override fun checkUsernameIsValid(userName: String) = validation.isUserNameValid(userName)

    override fun checkItemIsEmpty(input: String) =
        validation.isInputEmpty(input)

    override fun checkEmailAddressIsEmpty(inputTypeValue: String): String {
        email = inputTypeValue
        return validation.isInputEmpty(inputTypeValue)
    }

    override fun checkPasswordIsEmpty(inputTypeValue: String): String {
        password = inputTypeValue
        return validation.isInputEmpty(inputTypeValue)
    }

    override fun checkUserNameIsEmpty(inputTypeValue: String): String {
        userName = inputTypeValue
        return validation.isInputEmpty(inputTypeValue)
    }

    override fun checkFullNameIsEmpty(inputTypeValue: String): String {
        fullName = inputTypeValue
        return validation.isInputEmpty(inputTypeValue)
    }

}