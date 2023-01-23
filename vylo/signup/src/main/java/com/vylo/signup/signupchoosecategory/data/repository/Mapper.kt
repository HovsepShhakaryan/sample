package com.vylo.signup.signupchoosecategory.data.repository

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryData

class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun fromSearchCategoryDataToSearchCategoryItemList(categoryData: CategoryData) = categoryData.results

    fun getPagingQuery(url: String?): String? {
        return if (url != null) Uri.parse(url).getQueryParameter("cursor")
        else null
    }

}