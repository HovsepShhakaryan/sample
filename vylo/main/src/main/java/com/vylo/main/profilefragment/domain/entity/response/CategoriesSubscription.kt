package com.vylo.main.profilefragment.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.vylo.main.component.entity.SubCategoryItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoriesSubscription(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("global_id")
    val globalId: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null,
    @SerializedName("subcategories")
    val subcategories: List<SubCategoryItem>? = null,
    var isFollow: Boolean = true
) : Parcelable