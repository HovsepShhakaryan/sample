package com.vylo.main.settingsprivacy.presentation.fragment.about.presentation.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.adapter.entity.DrawableTextItem
import com.vylo.common.entity.RespondData
import com.vylo.common.entity.WebData
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.common.util.WEB_DATA
import com.vylo.main.R
import com.vylo.main.databinding.FragmentAboutBinding
import com.vylo.main.settingsprivacy.presentation.adapter.SettingsAdapter

class AboutFragment : BaseFragment<FragmentAboutBinding>() {

    override fun getViewBinding() = FragmentAboutBinding.inflate(layoutInflater)

    private val adapter: SettingsAdapter by lazy {
        SettingsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
        createToolbar()
        createContentOfView()
    }

    private fun createToolbar() {
        viewBinder.toolbar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack { backToPrevious() }
            setTitle(resources.getString(R.string.about))
            setStyleButtonBackText(com.vylo.common.R.style.MainText_H6_3)
            showBottomBorder(View.VISIBLE)
            setColorBottomBorder(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.shadow
                )
            )
            setHeightBottomBorder(com.vylo.common.R.dimen.half_one)
        }
    }

    private fun createContentOfView() {
        val backButton = ContextCompat.getDrawable(requireContext(), R.drawable.ic_next_button)
        val settingsList = listOf(
            DrawableTextItem(
                startText = resources.getString(R.string.terms_of_service),
                endImage = backButton,
                click = {
                    navigateTo(
                        R.id.action_aboutFragment_to_webFragment, bundleOf(
                            WEB_DATA to WebData(url = "https://vylo.com/terms")
                        )
                    )
                }
            ),
            DrawableTextItem(
                startText = resources.getString(R.string.privacy_policy),
                endImage = backButton,
                click = {
                    navigateTo(
                        R.id.action_aboutFragment_to_webFragment, bundleOf(
                            WEB_DATA to WebData(url = "https://vylo.com/privacy")
                        )
                    )
                }
            ),
//            DrawableTextItem(
//                startText = resources.getString(R.string.cookie_use),
//                endImage = backButton,
//                click = { showMessage("cookie") }
//            ),
//            DrawableTextItem(
//                startText = resources.getString(R.string.legal_notices),
//                endImage = backButton,
//                click = { showMessage("legal") }
//            ),
            DrawableTextItem(
                startText = resources.getString(R.string.contact_us),
                endImage = backButton,
                click = { showMessage("contact") }
            )
        )

        adapter.submitList(settingsList)
        viewBinder.apply {
            version.initialize(
                DrawableTextItem(
                    startText = resources.getString(R.string.app_version),
                    endText = getVersionName()
                )
            )

            sendCrashReport.initialize(
                DrawableTextItem(
                    startText = resources.getString(R.string.send_crash_reports),
                    isSwitch = true,
                    click = { isActive -> showMessage("$isActive") }
                )
            )

            list.adapter = adapter
            list.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        R.dimen.margin_padding_size_large_mid
                    ),
                    isBottomMargin = false
                )
            )
        }
    }

    private fun getVersionName(): String {
        return try {
            val pInfo =
                requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "0"
        }

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