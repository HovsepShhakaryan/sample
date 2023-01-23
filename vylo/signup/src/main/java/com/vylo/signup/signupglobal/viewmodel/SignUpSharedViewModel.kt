package com.vylo.signup.signupglobal.viewmodel

import androidx.lifecycle.ViewModel
import com.vylo.common.entity.SocialUserData
import com.vylo.common.util.enums.TokenType
import com.vylo.signup.signupglobal.entity.SignUp

class SignUpSharedViewModel : ViewModel() {

    private var signUp: SignUp? = null
    private var socialUserData: SocialUserData? = null
    private var tokenType: TokenType? = null

    fun setSignUp(signUp: SignUp?) {
        this.signUp = signUp
    }
    fun getSignUp() = signUp

    fun setSocialUserData(socialUserData: SocialUserData?) {
        this.socialUserData = socialUserData
    }
    fun getSocialUserData() = socialUserData

    fun setTokenType(tokenType: TokenType) {
        this.tokenType = tokenType
    }
    fun getTokenType() = tokenType
}