package com.vylo.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.vylo.common.util.GoogleAnalytics
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import org.koin.android.ext.android.inject

abstract class BaseBlurDialogFragment<B : ViewBinding> : SupportBlurDialogFragment() {

    protected lateinit var viewBinder: B
    private val baseViewModel: BaseViewModel by inject()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        @JvmStatic
        fun newInstance() = BaseBlurDialogFragment
    }

    abstract fun getViewBinding(): B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun isActionBarBlurred() = true
    override fun getBlurRadius() = 10

    protected fun sendAnalyticEvent(eventName: String, eventValue: String) {
        if (baseViewModel.isSendAnalytics()) {
            firebaseAnalytics = Firebase.analytics
            firebaseAnalytics.logEvent(eventName) {
                param(GoogleAnalytics.VALUE, eventValue)
            }
        }
    }

    protected fun backToPrevious() {
        activity?.onBackPressed()
    }
}