package com.vylo.signup.signupchoosecategory.domain.entity

import android.os.Parcelable
import com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChipList(
    val chipsOne: CategoryItem? = null,
    val chipsTwo: CategoryItem? = null
) : Parcelable
