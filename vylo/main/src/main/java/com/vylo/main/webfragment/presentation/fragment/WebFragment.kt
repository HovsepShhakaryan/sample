package com.vylo.main.webfragment.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import androidx.core.os.bundleOf
import com.vylo.common.BaseFragment
import com.vylo.common.entity.RespondData
import com.vylo.common.entity.VideoData
import com.vylo.common.entity.WebData
import com.vylo.common.ext.shareLink
import com.vylo.common.ext.toClipboard
import com.vylo.common.util.*
import com.vylo.common.util.enums.VideoType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.component.events.presentation.EventsViewModel
import com.vylo.main.databinding.FragmentWebBinding
import com.vylo.main.webfragment.domain.client.WebClient
import com.vylo.main.webfragment.presentation.viewmodel.WebViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebFragment : BaseFragment<FragmentWebBinding>() {

    private val viewModel by viewModel<WebViewModel>()
    private val eventViewModel by viewModel<EventsViewModel>()

    private lateinit var activity: MainFlawActivity
    private var webData: WebData? = null
    override fun getViewBinding() = FragmentWebBinding.inflate(layoutInflater)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onPause() {
        super.onPause()
        activity.hideProgress()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onResume() {
        super.onResume()
        hideNavBar()
    }

    private fun beginning() {
        activity = getActivity() as MainFlawActivity
        activity.showProgress()
        activity.hideNavBar()
        webData = arguments?.getParcelable(WEB_DATA)
        setWebViewSettings()
        createContentOfView()
        webData?.let {
            if (webData!!.globalId != null)
                eventViewModel.eventSourceView(mutableListOf(webData!!.globalId!!))
            else
                webData?.url?.let { url ->
                    viewBinder.webView.loadUrl(url)
                }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebViewSettings() {
        viewBinder.apply {
            val webClient = WebClient(activity::hideProgress)

            webView.webViewClient = webClient
            webView.webChromeClient = WebChromeClient()

            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true

            CookieManager.getInstance().setAcceptCookie(true)
        }
    }

    private fun createContentOfView() {
        viewModel.responseError.observe(viewLifecycleOwner) {
            hideProgress()
            it?.let { msg ->
                showMessage(msg)
            }
        }

        viewBinder.bottomNavBar.clickOnButtonBack { onBackClick() }
        viewBinder.bottomNavBar.responseListVisibility(View.GONE)

        webData?.globalId?.let {
            viewBinder.apply {
                bottomNavBar.clickOnKebab { onKebabClick() }
            }
        } ?: run {
            viewBinder.apply {
                bottomNavBar.kebabVisibility(View.GONE)
                bottomNavBar.buttonRespondVisibility(View.GONE)
                bottomNavBar.buttonResponsesVisibility(View.GONE)
            }
        }

//        webData?.url?.let { url ->
//            viewBinder.webView.loadUrl(url)
//        }
        webData?.globalId?.let { id ->
            viewModel.getFeedById(id)
        }
        viewModel.newsSuccess.observe(viewLifecycleOwner) {
            viewBinder.apply {
                viewBinder.webView.loadUrl(it.link!!)
                it.responseCounter?.let { rCount ->
                    bottomNavBar.setButtonResponsesTitle(rCount)
                    bottomNavBar.clickOnButtonRespond {
                        onRespondClick(
                            webData?.globalId,
                            webData?.title,
                            webData?.categoryId,
                            webData?.categoryName
                        )
                    }
                    if (rCount > 0) {
                        it.globalId?.let { id ->
                            bottomNavBar.clickOnButtonResponses {
                                onResponsesClick(VideoData(id, videoType = VideoType.RESPONSES))
                            }
                        }
                    }
                } ?: run {
                    bottomNavBar.buttonRespondVisibility(View.GONE)
                    bottomNavBar.buttonResponsesVisibility(View.GONE)
                }

            }
        }
        viewModel.newsError.observe(viewLifecycleOwner) {
            hideProgress()
            showMessage(it)
        }
    }

    private fun onBackClick() {
        viewBinder.let {
            if (it.webView.canGoBack()) {
                it.webView.goBack()
            } else {
                backToPrevious()
            }
        }
    }

    private fun onKebabClick() {
        KebabManager.createCommonWebKebab(
            activity = requireActivity(),
            globalId = webData?.globalId,
            externalLink = webData?.externalLink,
            onShareClick = ::onShareClick,
            onCopyLinkClick = ::onCopyLinkClick,
            onReportClick = ::onReportClick
        )
    }

    private fun onShareClick(id: String, link: String?) {
        link?.let {
            viewModel.shareReport(id)
            context?.shareLink(it)
        }
    }

    private fun onCopyLinkClick(id: String, link: String?) {
        link?.let {
            viewModel.shareReport(id)
            context?.toClipboard(it)
            showMessage(resources.getString(R.string.label_clipboard))
        }
    }

    private fun onReportClick(id: String) {
        KebabManager.createReportKebab(
            activity = requireActivity(),
            id = id,
            sendReport = { status, globalId ->
                viewModel.sendReport(status, globalId)
            }
        )
    }

    private fun onResponseListClick() {
        showMessage("onResponseListClick()")
    }

    private fun onRespondClick(
        id: String?,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
        val titleSheet = resources.getString(R.string.label_response)
        activity.openBottomSheetDialog(id, title, categoryId, categoryName, titleSheet)
    }

    private fun onResponsesClick(data: VideoData) {
        navigateTo(
            R.id.videoFragment, bundleOf(
                VIDEO_DATA to data
            )
        )
    }

    override fun openCreateResponseScreen(
        id: String?,
        responseType: Int,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
        super.openCreateResponseScreen(id, responseType, title, categoryId, categoryName)
        if (!id.isNullOrEmpty())
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPOND_INFO to RespondData(
                        responseToGlobalId = id,
                        title!!,
                        categoryId!!,
                        categoryName!!
                    ),
                    RESPONSE_TYPE to responseType
                )
            )
        else
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPONSE_TYPE to responseType
                )
            )
    }
}