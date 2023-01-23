package com.vylo.main.activity.data.repository

import com.vylo.common.api.Resource
import com.vylo.main.activity.domain.entity.NotificationToken

interface NotificationRepository {

    suspend fun pushNotificationToken(
        notificationToken: NotificationToken
    ): Resource<Unit>
}