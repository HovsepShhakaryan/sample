package com.vylo.signup.signupglobal.module

import com.vylo.signup.signupglobal.viewmodel.SignUpSharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val signUpSharedViewModule = module{

    viewModel {
        SignUpSharedViewModel()
    }
}