package com.vylo.main.homefragment.data

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.main.homefragment.domain.entity.response.FeedData

class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun fromFeedDataToFeedItemList(feedData: FeedData) = feedData.results

    fun getPagingQuery(url: String?): String? {
        return if (url != null) Uri.parse(url).getQueryParameter("cursor")
        else null
    }

    fun getPagingQueryWithPage(url: String?): Int? {
        return if (url != null) Uri.parse(url).getQueryParameter("page")?.toInt()
        else null
    }

}