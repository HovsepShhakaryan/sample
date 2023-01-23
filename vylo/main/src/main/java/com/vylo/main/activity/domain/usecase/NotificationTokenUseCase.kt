package com.vylo.main.activity.domain.usecase

import com.vylo.common.api.Resource
import com.vylo.main.activity.domain.entity.NotificationToken

interface NotificationTokenUseCase {

    suspend fun pushNotificationToken(
        notificationToken: NotificationToken
    ): Resource<Unit>
}