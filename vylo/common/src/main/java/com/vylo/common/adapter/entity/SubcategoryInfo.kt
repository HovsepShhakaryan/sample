package com.vylo.common.adapter.entity

data class SubcategoryInfo(
    val id: Long,
    val name: String,
    val subName: String? = null,
    val isFollow: Boolean
)
