package com.vylo.common.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoriesSubscription(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null,
    @SerializedName("subcategories")
    val subcategories: List<SubCategoryItem>? = null,
    var isFollow: Boolean = false
) : Parcelable

fun CategoriesSubscription.toCommonCategoryItem() = CommonCategoryItem(
    id = id,
    globalId = globalId,
    name = name,
    isActive = isActive,
    isFollow = isFollow,
    subCategories = if (subcategories.isNullOrEmpty()) null else subcategories.map { it.id }
)