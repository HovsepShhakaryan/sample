package com.vylo.common.ext

fun Int?.orZero() = this ?: 0

fun Long?.orZero() = this ?: 0

fun Int?.toBoolean() = when (this.orZero()) {
    0 -> false
    else -> true
}

fun Boolean?.orFalse() = this ?: false

fun List<String>.toText(): String {
    var text = ""
    this.forEach {
        text = it
    }
    return text
}