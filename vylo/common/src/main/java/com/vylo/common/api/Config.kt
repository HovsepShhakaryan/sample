package com.vylo.common.api

import android.app.Application

object Config {
    lateinit var BASE_URL_COLUMN_API: String
    lateinit var BASE_URL_COLUMN_FEED_API: String
    lateinit var BASE_URL_EVENTS_API: String
    lateinit var BASE_URL_UPLOAD_VIDEO: String
    lateinit var BASE_URL_UPLOAD_AUDIO: String
    lateinit var BASE_URL_UPLOAD_THUMBNAIL: String
    const val GOOGLE_CLIENT_ID = "136905785185-jgphqq5ai9a1o2a26bjbec3um7c93mvc.apps.googleusercontent.com"


    fun init(app: Application?) {
        //TODO
    }

    fun setBaseUrlColumnApi(url: String) { BASE_URL_COLUMN_API = url }
    fun setBaseUrlColumnFeedApi(url: String) { BASE_URL_COLUMN_FEED_API = url }
    fun setBaseUrlEventsApi(url: String) { BASE_URL_EVENTS_API = url }
    fun setBaseUrlVideoUploadApi(url: String) { BASE_URL_UPLOAD_VIDEO = url }
    fun setBaseUrlAudioUploadApi(url: String) { BASE_URL_UPLOAD_AUDIO = url }
    fun setBaseUrlThumbnailUploadApi(url: String) { BASE_URL_UPLOAD_THUMBNAIL = url }
}
