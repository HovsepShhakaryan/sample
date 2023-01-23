package com.vylo.androidapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.vylo.androidapplication.domain.module.versionModule
import com.vylo.androidapplication.presentation.activity.StartActivity
import com.vylo.auth.mainauthfragment.domain.modul.welcomeModule
import com.vylo.auth.signinfragment.domain.modul.authModule
import com.vylo.common.api.Config
import com.vylo.common.domain.module.commonModule
import com.vylo.main.activity.domain.module.mainActivityModule
import com.vylo.main.activityfragment.domain.module.activityModule
import com.vylo.main.categorymain.category.domain.module.categoryModule
import com.vylo.main.categorymain.categorytrendingfragment.domain.module.categoryTrendingModule
import com.vylo.main.component.events.domain.module.eventsModule
import com.vylo.main.component.sharedviewmodel.module.settingsSharedViewModel
import com.vylo.main.component.sharedviewmodel.module.sharedViewModule
import com.vylo.main.followmain.followfragment.domain.module.followModule
import com.vylo.main.followmain.followingfragment.domain.module.followingModule
import com.vylo.main.globalsearchmain.searchmain.domain.module.searchMainModule
import com.vylo.main.globalsearchmain.searchstartscreen.domain.modul.searchStartModule
import com.vylo.main.homefragment.domain.module.mainModule
import com.vylo.main.insightfulfragment.domain.module.insightfulModule
import com.vylo.main.navigation.domain.cameraModule
import com.vylo.main.newsstand.presentation.domain.module.newsstandModule
import com.vylo.main.profilefragment.domain.module.editProfileModule
import com.vylo.main.profilefragment.domain.module.profileModule
import com.vylo.main.responsemain.upload.domain.module.uploadVideoModule
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.domain.module.myAccountModule
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.domain.module.deleteAccountModule
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.module.updatePasswordModule
import com.vylo.main.trending.domain.module.trendingModule
import com.vylo.main.videofragment.domain.module.videoModule
import com.vylo.main.webfragment.domain.module.webModule
import com.vylo.resetpassword.forgotpasscode.domain.modul.forgotPassCodeModule
import com.vylo.resetpassword.forgotpassemail.domain.modul.forgotPassEmailModule
import com.vylo.signup.signupchoosecategory.domain.modul.signUpCategoryModule
import com.vylo.signup.signupcomplete.domain.modul.signUpModule
import com.vylo.signup.signupcreatepassword.domain.signUpCreatePasswordModule
import com.vylo.signup.signupcreateusername.domain.modul.signUpChooseUserNameModule
import com.vylo.signup.signupdonotmissout.domain.module.signUpDoNotMissOutModule
import com.vylo.signup.signupglobal.module.signUpSharedViewModule
import com.vylo.signup.signupinputdategender.domain.module.signUpInputGenderModule
import com.vylo.signup.signupinputemail.domain.modul.signUpInputEmailModule
import com.vylo.signup.signupprofileimage.domain.modul.profilePickModule
import com.vylo.signup.signupverificationcode.domain.modul.signUpCodeVerifyModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(), ContextProvider, CameraXConfig.Provider {

    private lateinit var applicationStart: String
    private lateinit var noInternet: String
    private var currentActivity: Activity? = null

    @SuppressLint("UnsafeOptInUsageError")
    override fun getCameraXConfig(): CameraXConfig {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
            .setMinimumLoggingLevel(Log.ERROR).build()
    }

    override fun onCreate() {
        super.onCreate()
        registerBroadcast()
        registerBroadcastForNetwork()
        Config.init(this)
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    commonModule,
                    authModule,
                    webModule,
                    mainModule,
                    videoModule,
                    newsstandModule,
                    cameraModule,
                    uploadVideoModule,
                    categoryModule,
                    sharedViewModule,
                    settingsSharedViewModel,
                    profileModule,
                    editProfileModule,
                    followModule,
                    followingModule,
                    searchMainModule,
                    searchStartModule,
                    myAccountModule,
                    updatePasswordModule,
                    welcomeModule,
                    signUpInputGenderModule,
                    signUpCreatePasswordModule,
                    signUpSharedViewModule,
                    signUpDoNotMissOutModule,
                    signUpModule,
                    profilePickModule,
                    trendingModule,
                    categoryTrendingModule,
                    deleteAccountModule,
                    forgotPassEmailModule,
                    signUpInputEmailModule,
                    signUpCodeVerifyModule,
                    signUpCategoryModule,
                    signUpChooseUserNameModule,
                    forgotPassCodeModule,
                    insightfulModule,
                    eventsModule,
                    activityModule,
                    mainActivityModule,
                    versionModule
                )
            )
        }

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityPaused(activity: Activity) {
                currentActivity = null
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

    }

    private fun returnToStartScreen() {
        val logOut = Intent(this, StartActivity::class.java)
        logOut.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(logOut)
    }

    private fun noInternetConnection() {
//        val activity = getActivityContext() as BaseActivity
//        activity.hideProgress()
//        activity.showNetworkErrorDialog()
    }

    private fun registerBroadcast() {
        applicationStart = applicationContext.getString(R.string.label_return_start_screen)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter(applicationStart))
    }

    private fun registerBroadcastForNetwork() {
        noInternet = applicationContext.getString(R.string.label_no_connection)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter(noInternet))
    }

    private fun unregisterBroadcast() {
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastReceiver)
    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                applicationStart -> returnToStartScreen()
                noInternet -> noInternetConnection()
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterBroadcast()
    }

    override fun getActivityContext(): Context? {
        return currentActivity
    }

}