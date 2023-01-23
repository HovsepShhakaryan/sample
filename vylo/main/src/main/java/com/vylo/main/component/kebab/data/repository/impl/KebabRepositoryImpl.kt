package com.vylo.main.component.kebab.data.repository.impl

import android.content.Context
import com.vylo.common.api.Resource
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.api.apiservis.ApiEvents
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.component.kebab.data.endpoint.KebabApi
import com.vylo.main.component.kebab.data.repository.KebabRepository
import com.vylo.main.component.kebab.domain.entity.request.BlockUser
import com.vylo.main.component.kebab.domain.entity.request.ReportRequest
import com.vylo.main.component.kebab.domain.entity.request.ViewRequest
import org.koin.java.KoinJavaComponent

class KebabRepositoryImpl(
    context: Context
) : BaseRepo(), KebabRepository {

    private val apiEvents: ApiEvents by KoinJavaComponent.inject(ApiEvents::class.java)
    private val clientEvents = apiEvents.getInstance(context).create(KebabApi::class.java)

    private val apiColumn: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val clientColumn = apiColumn.getInstance(context).create(KebabApi::class.java)

    override suspend fun getEventCounter(id: String) =
        safeApiCall { clientEvents.getEventCounter(id) }

    override suspend fun addLike(id: String) = safeApiCall { clientEvents.addLike(id) }

    override suspend fun deleteLike(id: String) = safeApiCall { clientEvents.deleteLike(id) }

    override suspend fun sendReport(report: ReportRequest) =
        safeApiCall { clientColumn.sendReport(report) }

    override suspend fun blockUser(blockUser: BlockUser) =
        safeApiCall { clientColumn.blockUser(blockUser) }

    override suspend fun shareReport(id: String) = safeApiCall { clientEvents.shareReport(id) }

    override suspend fun viewReport(id: String) = safeApiCall {
        clientEvents.viewReport(ViewRequest(listOf(id)))
    }
}