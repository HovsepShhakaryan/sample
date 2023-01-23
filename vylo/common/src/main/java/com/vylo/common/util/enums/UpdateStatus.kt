package com.vylo.common.util.enums

enum class UpdateStatus(val status: Long) {
    DRAFT(1),
    PUBLISHED(2),
    APPROVED(3),
    DECLINED(4),
    DELETED(5)
}