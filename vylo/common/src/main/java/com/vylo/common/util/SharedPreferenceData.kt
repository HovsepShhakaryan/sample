package com.vylo.common.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.vylo.common.util.enums.ScreenType

class SharedPreferenceData(context: Context) {

    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs_file",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    //_Key variables
    private val screenType: String = "screen_type"
    private val refreshToken: String = "refresh_token"
    private val token: String = "access_token"
    private val userGlobalId: String = "user_global_id"
    private val onboarding: String = "onboarding"
    private val pushNotificationToken: String = "notification_token"
    private val sendAnalytics: String = "send_analytics"
    private val partialToken: String = "partial_token"

    fun savePartialToken(partialToken: String) {
        sharedPreferences
            .edit()
            .putString(this.partialToken, partialToken)
            .apply()
    }

    val retrievePartialToken
        get() = sharedPreferences.getString(this.partialToken, null)

    fun removePartialToken() {
        sharedPreferences
            .edit()
            .putString(this.partialToken, null)
            .apply()
    }

    fun setAnalytics(isSendAnalytics: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(sendAnalytics, isSendAnalytics)
            .apply()
    }

    val isSendAnalytics
        get() = sharedPreferences.getBoolean(sendAnalytics, false)

    fun saveNotificationToken(notificationToken: String) {
        sharedPreferences
            .edit()
            .putString(pushNotificationToken, notificationToken)
            .apply()
    }

    val retrieveNotificationToken
        get() = sharedPreferences.getString(pushNotificationToken, null)

    fun removeScreenType() {
        sharedPreferences
            .edit()
            .putInt(screenType, 1)
            .apply()
    }

    fun saveScreenType(type: ScreenType) {
        sharedPreferences
            .edit()
            .putInt(screenType, type.type)
            .apply()
    }

    val retrieveScreenType
        get() = sharedPreferences.getInt(screenType, ScreenType.AUTH.type)

    fun removeToken() {
        sharedPreferences
            .edit()
            .putString(token, "")
            .apply()
    }

    fun saveToken(accessToken: String) {
        sharedPreferences
            .edit()
            .putString(token, accessToken)
            .apply()
    }

    val retrieveToken
        get() = sharedPreferences.getString(token, null)

    fun removeRefreshToken() {
        sharedPreferences
            .edit()
            .putString(refreshToken, "")
            .apply()
    }

    fun saveRefreshToken(refToken: String) {
        sharedPreferences
            .edit()
            .putString(refreshToken, refToken)
            .apply()
    }

    val retrieveRefreshToken
        get() = sharedPreferences.getString(refreshToken, null)

    fun removeUserGlobalId() {
        sharedPreferences
            .edit()
            .putString(userGlobalId, "")
            .apply()
    }

    fun saveUserGlobalId(globalId: String) {
        sharedPreferences
            .edit()
            .putString(userGlobalId, globalId)
            .apply()
    }

    val myGlobalId
        get() = sharedPreferences.getString(userGlobalId, null)

    fun setOnboarding(isShow: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(onboarding, isShow)
            .apply()
    }

    val isShowOnboarding
        get() = sharedPreferences.getBoolean(onboarding, true)
}