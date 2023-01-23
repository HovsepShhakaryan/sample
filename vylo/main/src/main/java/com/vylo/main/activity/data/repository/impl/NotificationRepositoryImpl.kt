package com.vylo.main.activity.data.repository.impl

import android.content.Context
import com.vylo.common.api.apiservis.ApiEvents
import com.vylo.common.data.repository.BaseRepo
import com.vylo.main.activity.data.endpoint.ActivityApi
import com.vylo.main.activity.data.repository.NotificationRepository
import com.vylo.main.activity.domain.entity.NotificationToken
import org.koin.java.KoinJavaComponent

class NotificationRepositoryImpl(
    context: Context
) : BaseRepo(), NotificationRepository {

    private val apiBase: ApiEvents by KoinJavaComponent.inject(ApiEvents::class.java)
    private val client = apiBase.getInstance(context).create(ActivityApi::class.java)

    override suspend fun pushNotificationToken(notificationToken: NotificationToken) =
        safeApiCall { client.pushNotificationToken(notificationToken) }
}