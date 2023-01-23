package com.vylo.main.globalsearchmain.searchstartscreen.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.main.globalsearchmain.searchstartscreen.domain.entiry.response.TrendingData

class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun getTrending(trendingData: List<TrendingData>): List<String> {
        val data = mutableListOf<String>()
        if (trendingData.isNotEmpty())
            for (item in trendingData)
                data.add(item.searchString!!)

        return data
    }
}