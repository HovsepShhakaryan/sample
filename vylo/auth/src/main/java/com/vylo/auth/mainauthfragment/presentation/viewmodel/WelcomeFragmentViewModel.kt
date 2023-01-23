package com.vylo.auth.mainauthfragment.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vylo.auth.R
import com.vylo.auth.mainauthfragment.domain.entity.request.Social
import com.vylo.auth.mainauthfragment.domain.entity.request.SocialItem
import com.vylo.auth.mainauthfragment.domain.entity.response.SocialData
import com.vylo.auth.mainauthfragment.domain.usecase.CreateTokenModelUseCase
import com.vylo.auth.mainauthfragment.domain.usecase.SignUpSocialUseCase
import com.vylo.auth.mainauthfragment.domain.usecase.TokensUseCase
import com.vylo.auth.mainauthfragment.domain.usecase.impl.TokensUseCaseImpl
import com.vylo.common.SingleLiveEvent
import com.vylo.common.api.Resource
import com.vylo.common.domain.usecase.GeneralErrorMapperUseCase
import com.vylo.common.util.enums.TokenType
import com.vylo.signup.signupcomplete.domain.entity.SignIn
import com.vylo.signup.signupcomplete.domain.usecase.SignUpUseCase
import kotlinx.coroutines.launch

class WelcomeFragmentViewModel(
    private val signUpSocialUseCase: SignUpSocialUseCase,
    private val generalErrorMapperUseCase: GeneralErrorMapperUseCase,
    private val createTokenModelUseCase: CreateTokenModelUseCase,
    private val tokensUseCase: TokensUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val context = application
    val responseError: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseSuccess: SingleLiveEvent<SocialData> by lazy { SingleLiveEvent() }
    val responseSuccessLogIn: SingleLiveEvent<String> by lazy { SingleLiveEvent() }

    fun sendSocialToken(token: String, type: TokenType) {
        val social = createTokenModelUseCase.createSocialTokenModel(token, type)
        viewModelScope.launch {
            when (val socialResponse = signUpSocialUseCase.invokeSignInSocial(type, social)
            ) {
                is Resource.Success -> {
                    if (socialResponse.data != null) {
                        val partialToken = socialResponse.data!!.partialToken
                        if (partialToken != null) {
                            tokensUseCase.savePartialToken(partialToken)
                            responseSuccess.postValue(socialResponse.data)
                        } else {
                            tokensUseCase.saveRefreshToken(socialResponse.data?.refresh!!)
                            tokensUseCase.saveToken(socialResponse.data?.access!!)
                            responseSuccessLogIn.postValue(context.resources.getString(com.vylo.signup.R.string.label_success_sign_in))
                        }
                    } else responseError.postValue(context.resources.getString(R.string.label_social_error))
                }
                is Resource.ApiError -> {
                    val apiErrorMessage = generalErrorMapperUseCase.getApiErrorMessage(socialResponse.errorData)
                    if (apiErrorMessage?.detail != null) responseError.postValue(apiErrorMessage.detail)
                    else responseError.postValue(context.resources.getString(com.vylo.signup.R.string.label_something_wrong))
                }
                is Resource.Error -> responseError.postValue(socialResponse.errorMessage)
            }
        }
    }
}