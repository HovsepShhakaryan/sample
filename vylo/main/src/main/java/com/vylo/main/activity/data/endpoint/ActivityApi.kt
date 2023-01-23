package com.vylo.main.activity.data.endpoint

import com.vylo.main.activity.domain.entity.NotificationToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ActivityApi {

    companion object {
        private const val PUSH_NOTIFICATION_TOKEN = "devices/"
    }

    @POST(PUSH_NOTIFICATION_TOKEN)
    suspend fun pushNotificationToken(
        @Body notificationToken: NotificationToken
    ): Response<Unit>
}