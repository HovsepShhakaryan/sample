package com.vylo.common.validation

import android.content.Context
import com.vylo.common.R
import java.util.regex.Pattern

class Validation(private val context: Context) {

    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^([+\\w-]+(?:\\.[+\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)\$"
        ).matcher(email).matches()
    }


    fun isValidEmailWithText(email: String): String {
        if (!isValidEmailId(email))
            return context.resources.getString(R.string.label_invalid_field)
        return ""
    }


    fun isInputEmpty(text: String): String {
        val input = text.trim()
        if (input.isBlank())
            return context.resources.getString(R.string.label_file_field)
        return ""
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordREGEX = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\!\\@\\#\\\$\\%\\^\\&\\*\\(\\)\\-\\_\\+\\=\\{\\}\\[\\]\\|\\\\/\\:\\;\\<\\>\\,\\.\\?])[A-Za-z\\d\\!\\@\\#\\\$\\%\\^\\&\\*\\(\\)\\-\\_\\+\\=\\{\\}\\[\\]\\|\\\\/\\:\\;\\<\\>\\,\\.\\?]{8,32}\$"
        )
        return passwordREGEX.matcher(password).matches()
    }


    fun isValidPasswordWithText(password: String): String {
        if (!isValidPassword(password))
            return context.resources.getString(R.string.label_invalid_field)
        return ""
    }


    fun isFieldsMatched(valueOne: String, valueTwo: String): String {
        if (valueOne != valueTwo)
            return context.resources.getString(R.string.label_pass_not_matched)
        return ""
    }

    private fun isUsernameValid(username: String): Boolean {
        return Pattern.compile(
            "^(?!\\w*(?i:vylo|admin))[\\w.]{3,15}\$"
        ).matcher(username).matches()
    }

    fun isUserNameValid(username: String): String {
        if (!isUsernameValid(username))
            return context.resources.getString(R.string.label_invalid_field)
        return ""
    }

}