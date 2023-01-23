package com.vylo.common.adapter.entity

data class KebabItem(
    val title: String? = null,
    val titleColorRes: Int? = null,
    val options: List<KebabOption>
)