package com.vylo.main.profilefragment.data

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.main.categorymain.categorytrendingfragment.domain.entity.response.SearchCategoryData
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsData

class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun fromProfileNewsDataToProfileNewsItemList(profileNewsData: ProfileNewsData) = profileNewsData.results

    fun getPagingQuery(url: String?): String? {
        return if (url != null) Uri.parse(url).getQueryParameter("cursor")
        else null
    }

}