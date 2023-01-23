package com.vylo.main.activity.domain.usecase.impl

import com.vylo.main.activity.data.repository.NotificationRepository
import com.vylo.main.activity.domain.entity.NotificationToken
import com.vylo.main.activity.domain.usecase.NotificationTokenUseCase

class NotificationTokenUseCaseImpl(
    private val notificationRepository: NotificationRepository
) : NotificationTokenUseCase {

    override suspend fun pushNotificationToken(notificationToken: NotificationToken) =
        notificationRepository.pushNotificationToken(notificationToken)
}