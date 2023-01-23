package com.vylo.main.responsemain.createvideo.domain.entity.response

data class ParametersOfVideoUpload(
    val host: String? = null,
    val path: String? = null,
    val awsAccessKeyId: String? = null,
    val signature: String? = null,
    val expires: String? = null,
    val newsRecordId: Long? = null,
    val newsRecordGlobalId: String? = null,
    val mediaRecordId: Long? = null
)