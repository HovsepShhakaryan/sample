package com.vylo.common.adapter.entity

import com.vylo.common.entity.CommonCategoryItem

data class CategoryInfo(
    val id: Long,
    val globalId: String,
    val name: String,
    val subName: String? = null,
    val isButtonFollowing: Boolean = true,
    val isFollow: Boolean = false,
    val categoriesList: List<SubcategoryInfo>? = null
)

fun CategoryInfo.toCommonCategoryItem() = CommonCategoryItem(
    id = id,
    globalId = globalId,
    name = name,
    isFollow = isFollow
)