package com.vylo.common.data

data class DataWrapper<T>(
    val next: String?,
    val previous: String?,
    val results: List<T>?
)