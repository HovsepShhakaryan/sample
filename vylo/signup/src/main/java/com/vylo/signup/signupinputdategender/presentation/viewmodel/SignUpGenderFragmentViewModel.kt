package com.vylo.signup.signupinputdategender.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.vylo.common.domain.usecase.ValidationUseCase

class SignUpGenderFragmentViewModel(
    private val validationUseCase: ValidationUseCase
) : ViewModel(){

    fun fullNameIsNotEmpty(name: String) =
        validationUseCase.checkFullNameIsEmpty(name)
}