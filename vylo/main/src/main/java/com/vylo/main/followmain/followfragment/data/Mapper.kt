package com.vylo.main.followmain.followfragment.data

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.main.categorymain.categorytrendingfragment.domain.entity.response.SearchCategoryData
import com.vylo.main.component.entity.PublishersSubscriptionData

class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun fromPublishersSubscriptionDataToPublishersSubscriptionList(publishersSubscriptionData: PublishersSubscriptionData) = publishersSubscriptionData.results

    fun getPagingQuery(url: String?): String? {
        return if (url != null) Uri.parse(url).getQueryParameter("cursor")
        else null
    }

}