package com.vylo.common.domain.usecase.impl

import android.net.Uri
import com.vylo.common.data.DataWrapper
import com.vylo.common.domain.usecase.PagingUseCase

class PagingUseCaseImpl : PagingUseCase {

    private var pagingToken: String? = null
    private var brakeCall = false

    override fun <T> fromDataToItemList(data: DataWrapper<T>): List<T>? {
        if (data.next != null) {
            pagingToken = data.next
        } else {
            brakeCall = true
        }
        return data.results
    }

    override fun getPagingToken() = getPagingQuery(pagingToken)

    override fun isBrakeCall() = brakeCall
    override fun setBrakeCall(isBrakeCall: Boolean) {
        pagingToken = null
        brakeCall = isBrakeCall
    }

    private fun getPagingQuery(url: String?): String? {
        return if (url != null) Uri.parse(url).getQueryParameter("cursor")
        else null
    }
}