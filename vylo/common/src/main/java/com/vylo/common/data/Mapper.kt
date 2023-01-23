package com.vylo.common.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.common.data.repository.error.*
import com.vylo.common.domain.localentity.entity.RecentTypes
import org.json.JSONArray
import org.json.JSONObject


class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun fromApiErrorToSignInErrorFromArrayCode(baseResponse: Any?): ResetPassError? {
        val json = gson.toJson(baseResponse)
        var data: ResetPassError? = null
        if (json != null) {
            data = gson.fromJson(json, genericType<ResetPassError>())
        }

        return data
    }

    fun fromApiErrorToSignInErrorFromArray(baseResponse: Any?): String? {
        val json = gson.toJson(baseResponse)
        var data: String? = null
        if (json != null) {
            val jsonArray = JSONArray(json)
            data = jsonArray.get(0) as String?
        }

        return data
    }

    fun fromApiErrorToStringEmail(baseResponse: Any?): String? {
        val json = gson.toJson(baseResponse)
        var data: String? = null
        if (json != null) {
            val jsonObject = JSONObject(json)
            val jsonArray = jsonObject.getJSONArray("email")
            data = jsonArray.get(0) as String?
        }

        return data
    }

    fun fromApiErrorToStringCode(baseResponse: Any?): String? {
        val json = gson.toJson(baseResponse)
        var data: String? = null
        if (json != null) {
            val jsonObject = JSONObject(json)
            if (jsonObject.has("non_field_errors")) {
                val jsonArray = jsonObject.getJSONArray("non_field_errors")
                data = jsonArray.get(0) as String?
            }
            else if (jsonObject.has("new_password")) {
                val jsonArray = jsonObject.getJSONArray("new_password")
                data = jsonArray.get(0) as String?
            }
            else if (jsonObject.has("re_new_password")) {
                val jsonArray = jsonObject.getJSONArray("re_new_password")
                data = jsonArray.get(0) as String?
            }
            else if (jsonObject.has("code")) {
                val jsonArray = jsonObject.getJSONArray("code")
                data = jsonArray.get(0) as String?
            }
        }

        return data
    }


    fun fromApiErrorToSignInError(baseResponse: Any?): GeneralError? {
        val json = gson.toJson(baseResponse)
        var data: GeneralError? = null
        if (json != null)
            data = gson.fromJson(json, genericType<GeneralError>())

        return data
    }

    fun fromRecentTypeToList(recentType: List<RecentTypes>): List<String> {
        val data = mutableListOf<String>()
        if (recentType.isNotEmpty())
            for (item in recentType)
                data.add(item.recent!!)

        return data
    }

    fun fromApiErrorToPersonalInformationError(baseResponse: Any?): PersonalInformationError? {
        val json = gson.toJson(baseResponse)
        var data: PersonalInformationError? = null
        if (json != null)
            data = gson.fromJson(json, genericType<PersonalInformationError>())

        return data
    }

    fun fromApiErrorToReportError(baseResponse: Any?): ReportError? {
        val json = gson.toJson(baseResponse)
        var data: ReportError? = null
        if (json != null)
            data = gson.fromJson(json, genericType<ReportError>())

        return data
    }

    fun fromApiErrorToPasswordError(baseResponse: Any?): PasswordError? {
        val json = gson.toJson(baseResponse)
        var data: PasswordError? = null
        if (json != null)
            data = gson.fromJson(json, genericType<PasswordError>())

        return data
    }

    fun fromApiErrorToEditProfileError(baseResponse: Any?): EditProfileError? {
        val json = gson.toJson(baseResponse)
        var data: EditProfileError? = null
        if (json != null)
            data = gson.fromJson(json, genericType<EditProfileError>())

        return data
    }

    fun fromApiErrorToUpdateNewsError(baseResponse: Any?): UpdateNewsError? {
        val json = gson.toJson(baseResponse)
        var data: UpdateNewsError? = null
        if (json != null)
            data = gson.fromJson(json, genericType<UpdateNewsError>())

        return data
    }

}