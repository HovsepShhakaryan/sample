package com.vylo.common.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommonCategoryItem(
    val id: Long? = null,
    val globalId: String? = null,
    val name: String? = null,
    val isActive: Boolean? = null,
    var isFollow: Boolean = false,
    val subCategories: List<Long?>? = null
) : Parcelable