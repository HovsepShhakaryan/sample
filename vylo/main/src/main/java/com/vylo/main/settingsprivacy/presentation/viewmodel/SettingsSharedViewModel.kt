package com.vylo.main.settingsprivacy.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vylo.main.component.entity.ProfileInfo

class SettingsSharedViewModel : ViewModel() {

    private var profileInfo = MutableLiveData<ProfileInfo>()

    fun setProfileInfo(info: ProfileInfo) {
        profileInfo.value = info
    }

    fun getProfileInfo() = profileInfo.value

}