package com.vylo.main.component.sharedviewmodel

import androidx.lifecycle.ViewModel

class ActivityFragmentSharedViewModel : ViewModel() {

    private var profileId: String? = null
    private var respondType: String? = null
    private var uploadType: String? = null

    fun setDeepLinkId(id: String?) {
        profileId = id
    }
    fun getDeepLinkId() = profileId

    fun setRespondType(type: String) {
        respondType = type
    }
    fun getRespondType() = respondType

    fun setUploadType(type: String) {
        uploadType = type
    }
    fun getUploadType() = uploadType
}