package com.vylo.common

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.vylo.common.util.GoogleAnalytics
import com.vylo.common.util.ThrowStartScreen
import com.vylo.common.util.enums.ScreenType
import org.koin.android.ext.android.inject

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    protected lateinit var viewBinder: B
    private val baseViewModel: BaseViewModel by inject()
    private var listenerNavBar = baseViewModel.getListener()
    protected val isShowOnboarding = baseViewModel.isShowOnboarding
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {

        @JvmStatic
        fun newInstance() = BaseFragment
    }

    abstract fun getViewBinding(): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun sendAnalyticEvent(eventName: String, eventValue: String) {
        if (baseViewModel.isSendAnalytics()) {
            firebaseAnalytics = Firebase.analytics
            firebaseAnalytics.logEvent(eventName) {
                param(GoogleAnalytics.VALUE, eventValue)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    protected fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    protected fun setScreenType(screenType: ScreenType) {
        baseViewModel.setScreenType(screenType)
    }

    protected fun throwStartScreen() {
        ThrowStartScreen(requireContext()).throwStartScreen()
    }

    protected fun navigateTo(screenId: Int, bundle: Bundle? = null) {
        Navigation.findNavController(viewBinder.root).navigate(screenId, bundle)
    }

    protected fun backToPrevious() {
        activity?.onBackPressed()
    }

    protected fun showNavBar() {
        if (listenerNavBar != null)
            listenerNavBar!!.showNavBar()
    }

    protected fun hideNavBar() {
        if (listenerNavBar != null)
            listenerNavBar!!.hideNavBar()
    }

    protected fun setIsResetPass(isResetPass: Boolean) {
        baseViewModel.setResetPass(isResetPass)
    }

    protected fun getIsResetPass() = baseViewModel.getResetPass()

    protected fun getMainGraph(): Boolean {
        return baseViewModel.getMainGraph()!!
    }

    protected fun setOnboarding(isShow: Boolean) {
        baseViewModel.setOnboarding(isShow)
    }

    open fun showProgress() {
        freesScreen()
    }

    open fun hideProgress() {
        unFreesScreen()
    }

    private fun freesScreen() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun unFreesScreen() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    open fun openCreateResponseScreen(
        id: String?,
        responseType: Int,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
    }

    fun showKeyboard(view: View, context: Context) {
        view.requestFocus()
        val imm: InputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    fun getDeepLinkId() = baseViewModel.getDeepLinkId()

    fun setDeepLink(id: String?) { baseViewModel.setDeepLinkId(id) }

    protected fun getCancelUpdate() = baseViewModel.getCancelUpdate()
    protected fun setCancelUpdate(isCancelUpdate: Boolean) {
        baseViewModel.setCancelUpdate(isCancelUpdate)
    }
}