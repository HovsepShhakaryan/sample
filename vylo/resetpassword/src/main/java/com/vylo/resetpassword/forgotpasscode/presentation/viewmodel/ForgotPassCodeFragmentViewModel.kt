package com.vylo.resetpassword.forgotpasscode.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.resetpassword.R
import com.vylo.resetpassword.forgotpasscode.domain.entity.ConfirmPass
import com.vylo.resetpassword.forgotpasscode.domain.usecase.ForgotPassCodeUseCase
import com.vylo.resetpassword.forgotpassemail.domain.entity.response.SendEmailData
import kotlinx.coroutines.launch

class ForgotPassCodeFragmentViewModel(
    private val validationUseCase: ValidationUseCase,
    private val forgotPassCodeUseCase: ForgotPassCodeUseCase,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun codeIsNotEmpty(code: String) =
        validationUseCase.checkItemIsEmpty(code)

    fun passwordIsValid(password: String) =
        validationUseCase.checkPasswordIsValid(password)

    fun getCode(code: String) {
        forgotPassCodeUseCase.getCode(code)
    }

    fun getPassword(password: String) {
        forgotPassCodeUseCase.getNewPassword(password)
    }

    fun getConfirmPassword(password: String) {
        forgotPassCodeUseCase.getConfirmNewPassword(password)
    }

    fun sendConfirmation(token: String) {
        val getRequestModel = forgotPassCodeUseCase.createConfirmModel(token)
        viewModelScope.launch {
            when (val checkEmailResponse = forgotPassCodeUseCase.invokeCodeConfirm(getRequestModel)) {
                is Resource.Success -> {
                    responseSuccess.postValue(context.resources.getString(R.string.label_success_changed_pass))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.fromApiErrorToStringCode(checkEmailResponse.errorData)
                    if (apiErrorMessage != null && apiErrorMessage.isNotEmpty()) responseError.postValue(apiErrorMessage)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(checkEmailResponse.errorMessage)
            }
        }
    }
}