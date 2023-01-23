package com.vylo.signup.signupdonotmissout.data

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.signup.signupdonotmissout.domain.entity.response.AutoFollowData

class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun fromAutoFollowDataToAutoFollowItemList(data: AutoFollowData) = data.results

    fun getPagingQuery(url: String?): String? {
        return if (url != null) Uri.parse(url).getQueryParameter("cursor")
        else null
    }

    fun getPagingQueryWithPage(url: String?): Int? {
        return if (url != null) Uri.parse(url).getQueryParameter("page")?.toInt()
        else null
    }

}