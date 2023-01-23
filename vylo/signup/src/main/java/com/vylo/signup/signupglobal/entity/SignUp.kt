package com.vylo.signup.signupglobal.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUp(
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("birthday_date")
    val birthday_date: String? = null,
    @SerializedName("is_remember_me")
    val isRememberMe: Boolean = true
) : Parcelable
