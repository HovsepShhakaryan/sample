package com.vylo.common.ext

fun generateRandomPassword(): String {
    val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var passWord = ""
    for (i in 0..25) {
        passWord += chars[Math.floor(Math.random() * chars.length).toInt()]
    }
    return passWord
}