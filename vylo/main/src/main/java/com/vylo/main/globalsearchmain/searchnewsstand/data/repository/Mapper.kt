package com.vylo.globalsearch.searchnewsstand.data.repository

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.globalsearch.searchnewsstand.domain.SearchNewsstandData
import com.vylo.globalsearch.searchvylo.domain.entity.response.SearchVyloData

class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun fromNewsstandDataToNewsstandItemList(searchNewsstandData: SearchNewsstandData) = searchNewsstandData.results

    fun getPagingQuery(url: String?): String? {
        return if (url != null) Uri.parse(url).getQueryParameter("cursor")
        else null
    }

}