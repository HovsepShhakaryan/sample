package com.vylo.main.responsemain.createvideo.domain.entity.response

data class ParametersOfUpload(
    val host: String? = null,
    val path: String? = null,
    val awsAccessKeyId: String? = null,
    val signature: String? = null,
    val expires: String? = null,
    val newsRecordId: Long? = null,
    val newsRecordGlobalId: String? = null,
    val responseToGlobalId: String? = null,
    val responseToTitle: String? = null,
    val responseCategoryId: String? = null,
    val responseCategoryName: String? = null,
    val mediaRecordId: Long? = null
)