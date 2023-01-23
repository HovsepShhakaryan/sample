package com.vylo.common.domain.localentity.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vylo.common.entity.PublishersSubscription

@Entity(tableName = "publishers_entity")
data class PublishersEntity(
    @PrimaryKey
    val id: Long,
    val globalId: String,
    val userName: String,
    val name: String,
    val isSource: Boolean,
    val isApproved: Boolean,
    val profilePhoto: String
)

fun PublishersEntity.toPublishersSubscription() = PublishersSubscription(
    id = id,
    globalId = globalId,
    username = userName,
    name = name,
    isSource = isSource,
    isApproved = isApproved,
    profilePhoto = profilePhoto
)