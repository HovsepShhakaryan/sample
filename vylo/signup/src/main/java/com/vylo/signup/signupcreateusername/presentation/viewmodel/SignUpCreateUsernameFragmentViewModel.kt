package com.vylo.signup.signupcreateusername.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.domain.usecase.ValidationUseCase
import com.vylo.signup.R
import com.vylo.signup.signupcreateusername.domain.usecase.SignUpUsernameUseCase
import kotlinx.coroutines.launch

class SignUpCreateUsernameFragmentViewModel(
    private val validationUseCase: ValidationUseCase,
    private val signUpUsernameUseCase: SignUpUsernameUseCase,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent() }

    fun usernameValidation(userName: String) =
        validationUseCase.checkUsernameIsValid(userName)

    fun checkUsername(userName: String) {
        val getRequestModel = getUsernameRequestModel(userName)
        viewModelScope.launch {
            when (val checkUsernameResponse = signUpUsernameUseCase.invokeUsername(getRequestModel)) {
                is Resource.Success -> {
                    if (checkUsernameResponse.data != null) {
                        when (checkUsernameResponse.data!!.isValid) {
                            true -> responseSuccess.postValue(checkUsernameResponse.data!!.isValid)
                            false -> responseError.postValue(context.resources.getString(R.string.label_user_name_unique))
                            else -> responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                        }
                    } else responseError.postValue(context.resources.getString(R.string.label_no_data_available))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(checkUsernameResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(checkUsernameResponse.errorMessage)
            }
        }
    }

    private fun getUsernameRequestModel(userName: String) =
        signUpUsernameUseCase.createUsernameData(userName)
}