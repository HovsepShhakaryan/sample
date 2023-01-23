package com.vylo.signup.signupwebview.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import com.vylo.common.BaseFragment
import com.vylo.common.util.OPEN_WEB_FROM
import com.vylo.common.util.WEB_DATA
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpWebViewBinding


class SignUpWebViewFragment : BaseFragment<FragmentSignUpWebViewBinding>() {

    override fun getViewBinding() = FragmentSignUpWebViewBinding.inflate(layoutInflater)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
        createToolBar()
        createContentOfView()
    }

    private fun createToolBar() {
        val webData = arguments?.getBoolean(OPEN_WEB_FROM)
        viewBinder.toolbar.setIconOfButtonBack(R.drawable.ic_circleback)
        val buttonBack = View.OnClickListener {
            when (webData) {
                true -> navigateTo(R.id.action_signUpWebViewFragment_to_signUpVerificationCodeFragment)
                false -> navigateTo(R.id.action_signUpWebViewFragment_to_signUpCompleteFragment)
                else -> {}
            }
        }
        viewBinder.toolbar.clickOnButtonBack(buttonBack)
    }

    private fun createContentOfView() {
        val webData = arguments?.getString(WEB_DATA)
        setWebViewSettings(webData!!)
    }

    private fun setWebViewSettings(webData: String) {
        viewBinder.apply {
            webView.webChromeClient = WebChromeClient()

            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.loadUrl(webData)

            CookieManager.getInstance().setAcceptCookie(true)
        }
    }

}