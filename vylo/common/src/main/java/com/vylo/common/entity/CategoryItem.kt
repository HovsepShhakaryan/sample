package com.vylo.common.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryItem(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null,
    @SerializedName("subcategories")
    val subcategories: List<SubCategoryItem>? = null
) : Parcelable