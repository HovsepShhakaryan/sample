package com.vylo.main.categorymain.category.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    val id: String,
    val categoryName: String
)  : Parcelable
