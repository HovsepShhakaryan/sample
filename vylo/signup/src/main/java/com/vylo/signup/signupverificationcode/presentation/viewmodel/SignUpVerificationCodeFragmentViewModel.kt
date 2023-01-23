package com.vylo.signup.signupverificationcode.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.signup.R
import com.vylo.signup.signupverificationcode.domain.usecase.CodeVerifyUseCase
import kotlinx.coroutines.launch

class SignUpVerificationCodeFragmentViewModel(
    private val validationUseCase: ValidationUseCase,
    private val codeVerifyUseCase: CodeVerifyUseCase,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent() }

    fun codeIsEmpty(code: String) =
        validationUseCase.checkFullNameIsEmpty(code)

    fun sendVerificationCode(code: String, email: String) {
        val getRequestModel = getEmailRequestModel(email)
        viewModelScope.launch {
            when (val checkEmailResponse = codeVerifyUseCase.invokeCode(code, getRequestModel)) {
                is Resource.Success -> {
                    if (checkEmailResponse.data != null) {
                        when (checkEmailResponse.data!!.isValid) {
                            true -> responseSuccess.postValue(checkEmailResponse.data!!.isValid)
                            false -> responseError.postValue(context.resources.getString(R.string.label_user_verified))
                            else -> responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                        }
                    } else responseError.postValue(context.resources.getString(R.string.label_no_data_available))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(checkEmailResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(checkEmailResponse.errorMessage)
            }
        }
    }

    private fun getEmailRequestModel(email: String) =
        codeVerifyUseCase.createEmailData(email)
}