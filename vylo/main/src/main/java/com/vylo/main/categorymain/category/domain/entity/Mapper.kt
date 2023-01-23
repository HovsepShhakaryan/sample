package com.vylo.main.categorymain.category.domain.entity

import com.vylo.common.entity.CommonCategoryItem
import com.vylo.main.globalsearchmain.searchcategories.domain.entity.response.toCommonCategory


class Mapper {

    fun fromCategoryToCategoryItem(categories: List<Category>): List<CommonCategoryItem> {
        val categoryItems = mutableListOf<CommonCategoryItem>()
        categoryItems.addAll(categories.map { it.toCommonCategoryItem() })
        categories.map {
            it.subcategories?.map { it.toCommonCategory() }?.let {
                categoryItems.addAll(it)
            }
        }

        return categoryItems
    }
}