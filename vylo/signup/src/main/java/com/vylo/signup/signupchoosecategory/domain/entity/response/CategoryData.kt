package com.vylo.signup.signupchoosecategory.domain.entity.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryData(
    @SerializedName("next")
    val next: String? = null,
    @SerializedName("previous")
    val previous: String? = null,
    @SerializedName("results")
    val results: List<CategoryItem>? = null,
) : Parcelable

@Parcelize
data class CategoryItem(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("global_id")
    val global_id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null
) : Parcelable