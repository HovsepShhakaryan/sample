package com.vylo.auth.signinfragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vylo.auth.R
import com.vylo.auth.signinfragment.domain.usecase.impl.AuthValidationUseCaseImpl
import com.vylo.common.domain.usecase.impl.GeneralErrorMapperUseCaseImpl
import com.vylo.auth.signinfragment.domain.usecase.impl.SignInUseCaseImpl
import com.vylo.common.api.Resource
import kotlinx.coroutines.launch

class SignInFragmentViewModel(
    private val signInUseCaseImpl: SignInUseCaseImpl,
    private val authValidationUseCaseImpl: AuthValidationUseCaseImpl,
    private val generalErrorMapperUseCaseImpl: GeneralErrorMapperUseCaseImpl,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseError: MutableLiveData<String> by lazy { MutableLiveData() }
    val responseSuccess: MutableLiveData<String> by lazy { MutableLiveData() }

    fun signInCall() {
        viewModelScope.launch {
            val signInRequestObject = authValidationUseCaseImpl.requestSignInModel()
            when (val signInResponse = signInUseCaseImpl.invokeSignIn(signInRequestObject)) {
                is Resource.Success -> {
                    if (signInResponse.data != null) {
                        signInUseCaseImpl.saveRefreshToken(signInResponse.data?.refresh!!)
                        signInUseCaseImpl.saveToken(signInResponse.data?.access!!)
                        responseSuccess.postValue(context.getString(R.string.label_success_sign_in))
                    }
                    else responseError.postValue(context.resources.getString(R.string.label_no_data))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCaseImpl.getApiErrorMessage(signInResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(com.vylo.signup.R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(signInResponse.errorMessage)
            }
        }
    }

    fun checkEmailAddressIsEmpty(inputTypeValue: String) =
        authValidationUseCaseImpl.checkEmailAddressIsEmpty(inputTypeValue)

    fun checkPasswordIsEmpty(inputTypeValue: String) =
        authValidationUseCaseImpl.checkPasswordIsEmpty(inputTypeValue)

    fun checkEmailAddressIsValid(inputTypeValue: String) =
        authValidationUseCaseImpl.checkEmailAddressIsValid(inputTypeValue)

    fun checkPasswordIsValid(inputTypeValue: String) =
        authValidationUseCaseImpl.checkPasswordIsValid(inputTypeValue)
}