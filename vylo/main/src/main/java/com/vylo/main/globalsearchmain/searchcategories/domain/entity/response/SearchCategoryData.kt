package com.vylo.main.globalsearchmain.searchcategories.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.vylo.common.entity.CommonCategoryItem
import com.vylo.common.entity.SubCategoryItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchCategoryData(
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<SearchCategoryItem>? = null,
) : Parcelable

@Parcelize
data class SearchCategoryItem(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("global_id")
    val global_id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null,
    @SerializedName("subcategories")
    val subcategories: List<SubCategoryItem>? = null,
    var isFollow: Boolean = false
) : Parcelable

fun SearchCategoryItem.toCommonCategory() = CommonCategoryItem(
    id = id,
    globalId = global_id,
    name = name,
    isActive = isActive,
    isFollow = isFollow,
    subCategories = if (subcategories.isNullOrEmpty()) null else subcategories.map { it.id }
)

fun SubCategoryItem.toCommonCategory() = CommonCategoryItem(
    id = id,
    globalId = globalId,
    name = name,
    isActive = isActive,
    isFollow = isFollow
)