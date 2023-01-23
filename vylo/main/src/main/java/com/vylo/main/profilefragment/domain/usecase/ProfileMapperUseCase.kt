package com.vylo.main.profilefragment.domain.usecase

import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsData
import com.vylo.main.profilefragment.domain.entity.response.ProfileNewsItem

interface ProfileMapperUseCase {
    fun fromProfileNewsDataToProfileNewsItemList(profileNewsData: ProfileNewsData): List<ProfileNewsItem>?
    fun getPagingToken(): String?
}