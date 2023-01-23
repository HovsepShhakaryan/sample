package com.vylo.signup.signupcreatepassword.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.vylo.common.domain.usecase.ValidationUseCase

class SignUpCreatePasswordFragmentViewModel(
    private val validationUseCase: ValidationUseCase
) : ViewModel() {

    fun passwordIsValid(password: String) =
        validationUseCase.checkPasswordIsValid(password)
}