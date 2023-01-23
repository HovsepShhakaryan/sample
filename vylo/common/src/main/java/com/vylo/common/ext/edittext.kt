package com.vylo.common.ext

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText

fun EditText.setPasswordVisibility(isVisible: Boolean) {
    transformationMethod = if (isVisible) {
        HideReturnsTransformationMethod.getInstance()
    } else {
        PasswordTransformationMethod.getInstance()
    }
    setSelection(text.length)
}