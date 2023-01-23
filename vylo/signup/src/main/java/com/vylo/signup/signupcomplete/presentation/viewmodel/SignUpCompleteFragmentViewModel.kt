package com.vylo.signup.signupcomplete.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.entity.ProfileItem
import com.vylo.signup.R
import com.vylo.signup.signupcomplete.domain.entity.SignIn
import com.vylo.signup.signupcomplete.domain.entity.UserData
import com.vylo.signup.signupcomplete.domain.usecase.SignUpUseCase
import com.vylo.signup.signupglobal.entity.SignUp
import kotlinx.coroutines.launch

class SignUpCompleteFragmentViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<SignUp> by lazy { SingleLiveEvent() }
    val responseUpdateSuccess: SingleLiveEvent<ProfileItem> by lazy { SingleLiveEvent() }
    val responseSuccessSignIn: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    private lateinit var retrievePartialToken: String

    fun updateUser(userData: UserData) {
        viewModelScope.launch {
            when (val signUpRequest = signUpUseCase.updateUserData(userData)) {
                is Resource.Success -> {
                    if (signUpRequest.data != null) {
                        responseUpdateSuccess.postValue(signUpRequest.data)
                    } else responseError.postValue(context.resources.getString(R.string.label_no_data_available))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(signUpRequest.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(signUpRequest.errorMessage)
            }
        }
    }

    fun signUp(signUp: SignUp) {
        viewModelScope.launch {
            when (val signUpRequest = signUpUseCase.invokeSignUp(signUp)) {
                is Resource.Success -> {
                    if (signUpRequest.data != null) {
                        responseSuccess.postValue(signUpRequest.data)
                    } else responseError.postValue(context.resources.getString(R.string.label_no_data_available))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(signUpRequest.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(signUpRequest.errorMessage)
            }
        }
    }

    fun signIn(signIn: SignIn) {
        viewModelScope.launch {
            when (val signInResponse = signUpUseCase.invokeSignIn(signIn)) {
                is Resource.Success -> {
                    if (signInResponse.data != null) {
                        signUpUseCase.saveRefreshToken(signInResponse.data?.refresh!!)
                        signUpUseCase.saveToken(signInResponse.data?.access!!)
                        responseSuccessSignIn.postValue(context.resources.getString(R.string.label_success_sign_in))
                    }
                    else responseError.postValue(context.resources.getString(R.string.label_no_data))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(signInResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(signInResponse.errorMessage)
            }
        }
    }

    fun signInFacebook(userData: UserData) {
        viewModelScope.launch {
            retrievePartialToken = signUpUseCase.retrievePartialToken()!!
            when (val signInResponse = signUpUseCase.signInFacebook(retrievePartialToken, userData)) {
                is Resource.Success -> {
                    if (signInResponse.data != null) {
                        signUpUseCase.saveRefreshToken(signInResponse.data?.refresh!!)
                        signUpUseCase.saveToken(signInResponse.data?.access!!)
                        responseSuccessSignIn.postValue(context.resources.getString(R.string.label_success_sign_in))
                    }
                    else responseError.postValue(context.resources.getString(R.string.label_no_data))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(signInResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(signInResponse.errorMessage)
            }
        }
    }

    fun signInGoogle(userData: UserData) {
        viewModelScope.launch {
            retrievePartialToken = signUpUseCase.retrievePartialToken()!!
            when (val signInResponse = signUpUseCase.signInGoogle(retrievePartialToken, userData)) {
                is Resource.Success -> {
                    if (signInResponse.data != null) {
                        signUpUseCase.saveRefreshToken(signInResponse.data?.refresh!!)
                        signUpUseCase.saveToken(signInResponse.data?.access!!)
                        responseSuccessSignIn.postValue(context.resources.getString(R.string.label_success_sign_in))
                    }
                    else responseError.postValue(context.resources.getString(R.string.label_no_data))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(signInResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(signInResponse.errorMessage)
            }
        }
    }
}