package com.vylo.main.component.entity.mappers

import com.vylo.main.component.entity.ProfileInfo
import com.vylo.main.component.entity.ProfileItem

fun ProfileItem.toProfileInfo() = ProfileInfo(
    id = id,
    username = username,
    email = email,
    profilePhoto = profilePhoto,
    name = name,
    website = website,
    bio = bio,
    phone = phone,
    gender = gender,
    birthdayDate = birthdayDate
)