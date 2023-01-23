package com.vylo.common

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.vylo.common.widget.BottomSheetVersionControlDialog
import org.koin.android.ext.android.inject


abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    protected lateinit var viewBinder: B
    abstract fun getViewBinding(): B
    private val baseViewModel: BaseViewModel by inject()
    val screenType get() = baseViewModel.getScreenType
    private var data: Bundle? = null
    private lateinit var bottomSheet: BottomSheetVersionControlDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = getViewBinding()
        setContentView(viewBinder.root)
    }

    fun replaceActivity(activity: AppCompatActivity) {
        startActivity(Intent(this, activity::class.java))
    }

    protected fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    open fun showProgress() {}

    open fun hideProgress() {}

    protected fun freesScreen() {
        this.window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    protected fun unFreesScreen() {
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun setData(bundle: Bundle?) {
        data = bundle
    }

    fun getData() = data

    fun setDeepLinkId(id: String) {
        baseViewModel.setDeepLinkId(id)
    }

    protected fun savePushNotificationToken(token: String) {
        baseViewModel.savePushNotificationToken(token)
    }

    protected fun getPushNotificationToken() = baseViewModel.getPushNotificationToken()

    protected fun setAnalytics(isSendAnalytics: Boolean) {
        baseViewModel.setAnalytics(isSendAnalytics)
    }

    protected fun showVersionBottomSheet() {
        bottomSheet = BottomSheetVersionControlDialog(applicationContext)
        bottomSheet.show(
            supportFragmentManager,
            "bottomSheetVersionControl"
        )
        bottomSheet.isCancelable = false
    }

    protected fun dismissBottomSheet() {
        bottomSheet.dismiss()
    }

    protected fun getCancelUpdate() = baseViewModel.getCancelUpdate()
    protected fun setCancelUpdate(isCancelUpdate: Boolean) {
        baseViewModel.setCancelUpdate(isCancelUpdate)
    }

}