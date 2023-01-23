package com.vylo.globalsearch.searchcategories.data.repository

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.SearchCategoryData

class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun fromSearchCategoryDataToSearchCategoryItemList(searchCategoryData: SearchCategoryData) = searchCategoryData.results

    fun getPagingQuery(url: String?): String? {
        return if (url != null) Uri.parse(url).getQueryParameter("cursor")
        else null
    }

}