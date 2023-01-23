package com.vylo.main.categorymain.category.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.entity.SubCategoryItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categories(
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<Category>? = null
) : Parcelable

@Parcelize
data class Category(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("global_id")
    val global_id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("subcategories")
    val subcategories: List<SubCategoryItem>? = null,
    @SerializedName("is_active")
    val is_active: Boolean? = null
) : Parcelable

fun Category.toCommonCategoryItem() = CommonCategoryItem(
    id = id,
    globalId = global_id,
    name = name,
    isActive = is_active
)