package com.vylo.androidapplication.presentation.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.vylo.androidapplication.BuildConfig
import com.vylo.androidapplication.R
import com.vylo.androidapplication.databinding.ActivityStartBinding
import com.vylo.androidapplication.presentation.viewmodel.StartActivityViewModel
import com.vylo.auth.activity.AuthActivity
import com.vylo.common.BaseActivity
import com.vylo.common.SplashFragment
import com.vylo.common.UpdateFragment
import com.vylo.common.api.Config
import com.vylo.common.util.DEEP_LINK_ID
import com.vylo.common.util.enums.ScreenType
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class StartActivity : BaseActivity<ActivityStartBinding>() {

    private val viewModel by viewModel<StartActivityViewModel>()

    companion object {
        private const val TAG = "StartActivity"
        private const val DEEP_LINK = "deeplink"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beginning()
    }

    private fun beginning() {
        setBaseUrls()

        if (!getCancelUpdate()) {
            viewModel.getAppVersionName()
            viewModel.responseSuccess.observe(this) {
                if (it != null) checkingUpdate(it)
                else startDefault()
            }
            viewModel.responseError.observe(this) {
                if (it != null)
                    startDefault()
            }
        } else startDefault()
    }

    private fun checkingUpdate(remoteAppVersion: String) {

        if (getCancelUpdate()) {
            startDefault()
        } else {
            try {
                val pm = applicationContext.packageManager
                val pkgName = applicationContext.packageName
                val pkgInfo = pm.getPackageInfo(pkgName, 0)
                val curentVersion = pkgInfo!!.versionName

                if (remoteAppVersion[0] > curentVersion[0] || remoteAppVersion[2] > curentVersion[2]) {
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_container_view, SplashFragment()).commit()
                    showVersionBottomSheet()
                } else if (remoteAppVersion[4] > curentVersion[4]) {
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_container_view, UpdateFragment()).commit()
                } else startDefault()

            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun startDefault() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_container_view, SplashFragment()).commit()
        getFirebaseToken()
        getDynamicLink(intent)
    }

    override fun getViewBinding() = ActivityStartBinding.inflate(layoutInflater)

    private fun navigateToNextScreen(deepLinkId: String?) {
        when (screenType) {
            ScreenType.AUTH.type -> {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
            ScreenType.MAIN.type -> {
                val intent = Intent(this, MainFlawActivity::class.java)
                intent.putExtra(DEEP_LINK_ID, deepLinkId)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setBaseUrls() {
        Config.setBaseUrlColumnApi(BuildConfig.BASE_URL_COLUMN_API)
        Config.setBaseUrlColumnFeedApi(BuildConfig.BASE_URL_COLUMN_FEED_API)
        Config.setBaseUrlEventsApi(BuildConfig.BASE_URL_EVENTS_API)
        Config.setBaseUrlVideoUploadApi(BuildConfig.BASE_URL_UPLOAD_VIDEO)
        Config.setBaseUrlAudioUploadApi(BuildConfig.BASE_URL_UPLOAD_AUDIO)
        Config.setBaseUrlThumbnailUploadApi(BuildConfig.BASE_URL_UPLOAD_THUMBNAIL)
        setAnalytics(BuildConfig.BASE_URL_EVENTS_API == com.vylo.androidapplication.Config.BASE_URL_EVENTS_API)
    }

    private fun getDynamicLink(intent: Intent) {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                var deepLinkId: String? = null

                if (pendingDynamicLinkData != null)
                    deepLinkId = pendingDynamicLinkData.link.toString()
                else if (intent.extras != null)
                    deepLinkId = intent.extras?.get(DEEP_LINK).toString()

                navigateToNextScreen(deepLinkId)
            }
            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                Log.d(TAG, "retrieve token successful : $token")
                savePushNotificationToken(token)
            } else {
                Log.w(TAG, "token should not be null...")
            }
        }.addOnFailureListener { e: Exception? -> }.addOnCanceledListener {}.addOnCompleteListener()
        { task: Task<String> ->
            Log.v(TAG, "This is the token : " + task.result)
        }
    }
}