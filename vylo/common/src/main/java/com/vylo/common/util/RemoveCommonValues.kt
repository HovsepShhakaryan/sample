package com.vylo.common.util

private fun <T> removeCommonValues(listA: List<T>, listB: List<T>): List<T> {
    val outA = listA.toMutableList()
    val outB = listB.toMutableList()
    for (i in (outA.size - 1) downTo 0) {
        for (j in (outB.size - 1) downTo 0) {
            if (outA[i] == outB[j]) {
                outA.removeAt(i)
                outB.removeAt(j)
                break
            }
        }
    }

    return outA + outB
}