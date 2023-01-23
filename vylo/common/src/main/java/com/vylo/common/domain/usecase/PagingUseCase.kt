package com.vylo.common.domain.usecase

import com.vylo.common.data.DataWrapper

interface PagingUseCase {
    fun <T> fromDataToItemList(data: DataWrapper<T>): List<T>?
    fun getPagingToken(): String?
    fun isBrakeCall(): Boolean
    fun setBrakeCall(isBrakeCall: Boolean)
}