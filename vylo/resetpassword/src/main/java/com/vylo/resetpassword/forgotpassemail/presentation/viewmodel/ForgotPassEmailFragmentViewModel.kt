package com.vylo.resetpassword.forgotpassemail.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.resetpassword.R
import com.vylo.resetpassword.forgotpassemail.domain.entity.response.SendEmailData
import com.vylo.resetpassword.forgotpassemail.domain.usecase.ForgotPassUseCase
import kotlinx.coroutines.launch

class ForgotPassEmailFragmentViewModel(
    private val validationUseCase: ValidationUseCase,
    private val forgotPassUseCase: ForgotPassUseCase,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application){

    private val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<SendEmailData> by lazy { SingleLiveEvent() }

    fun emailIsValid(emailAddress: String) =
        validationUseCase.checkEmailAddressIsValid(emailAddress)

    fun sendEmail(email: String) {
        val getRequestModel = getEmailRequestModel(email)
        viewModelScope.launch {
            when (val checkEmailResponse = forgotPassUseCase.invokeEmail(getRequestModel)) {
                is Resource.Success -> {
                    if (checkEmailResponse.data != null) {
                        responseSuccess.postValue(checkEmailResponse.data)
                    } else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.fromApiErrorToStringEmail(checkEmailResponse.errorData)
                    if (apiErrorMessage != null && apiErrorMessage.isNotEmpty()) responseError.postValue(apiErrorMessage)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(checkEmailResponse.errorMessage)
            }
        }
    }

    private fun getEmailRequestModel(email: String) =
        forgotPassUseCase.createEmailData(email)
}