package com.vylo.main.globalsearchmain.searchprofiles.data.repository

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vylo.common.entity.PublishersSubscription
import com.vylo.main.globalsearchmain.searchprofiles.domain.entity.response.ProfileData

class Mapper(private val gson: Gson) {

    private inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    fun fromSearchProfileDataToSearchProfileItemList(searchProfileData: ProfileData) = searchProfileData.results

    fun fromSearchProfileDataToPublishersSubscriptionList(searchProfileData: ProfileData) =
        searchProfileData.results?.map {
            PublishersSubscription(
                id = it.id,
                globalId = it.globalId,
                username = it.username,
                name = it.name,
                isSource = it.isSource,
                isApproved = it.isApproved,
                profilePhoto = it.profilePhoto
            )
        }

    fun getPagingQuery(url: String?): String? {
        return if (url != null) Uri.parse(url).getQueryParameter("page")
        else null
    }

}