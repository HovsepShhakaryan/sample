package com.vylo.common

import androidx.lifecycle.ViewModel
import com.vylo.common.util.SharedPreferenceData
import com.vylo.common.util.enums.ScreenType

open class BaseViewModel(
    private val sharedPreferenceData: SharedPreferenceData,
) : ViewModel() {

    val getScreenType get() = sharedPreferenceData.retrieveScreenType
    val isShowOnboarding get() = sharedPreferenceData.isShowOnboarding
    private var navBarListener: ShowHideNavBar? = null
    private var isMakeResetPass = false
    private var isProfileGraph: Boolean? = null
    private var deepLinkId: String? = null
    private var isCancelUpdate: Boolean = false

    fun setCancelUpdate(isCancelUpdate: Boolean) {
        this.isCancelUpdate = isCancelUpdate
    }
    fun getCancelUpdate() = isCancelUpdate

    fun setAnalytics(isSendAnalytics: Boolean) {
        sharedPreferenceData.setAnalytics(isSendAnalytics)
    }
    fun isSendAnalytics() = sharedPreferenceData.isSendAnalytics

    fun setDeepLinkId(id: String?) {
        deepLinkId = id
    }
    fun getDeepLinkId() = deepLinkId

    fun setResetPass(isMakeResetPass: Boolean) {
        this.isMakeResetPass = isMakeResetPass
    }

    fun getResetPass() = isMakeResetPass

    fun setScreenType(screenType: ScreenType) {
        sharedPreferenceData.saveScreenType(screenType)
    }

    fun setNavBarListener(navBarListener: ShowHideNavBar) {
        this.navBarListener = navBarListener
    }

    fun getListener() = navBarListener

    fun setMainGraph(isProfileGraph: Boolean) {
        this.isProfileGraph = isProfileGraph
    }

    fun getMainGraph() = isProfileGraph

    fun setOnboarding(isShow: Boolean) {
        sharedPreferenceData.setOnboarding(isShow)
    }

    fun savePushNotificationToken(token: String) {
        sharedPreferenceData.saveNotificationToken(token)
    }

    fun getPushNotificationToken() = sharedPreferenceData.retrieveNotificationToken
}