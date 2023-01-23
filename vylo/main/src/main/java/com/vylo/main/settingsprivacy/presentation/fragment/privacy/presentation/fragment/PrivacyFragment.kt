package com.vylo.main.settingsprivacy.presentation.fragment.privacy.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.entity.DrawableTextItem
import com.vylo.common.entity.RespondData
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.main.R
import com.vylo.main.databinding.FragmentPrivacyBinding

class PrivacyFragment : BaseFragment<FragmentPrivacyBinding>() {

    override fun getViewBinding() = FragmentPrivacyBinding.inflate(layoutInflater)

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
            setTitle(resources.getString(R.string.privacy))
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
        viewBinder.apply {
            responsesAccount.initialize(DrawableTextItem(
                startText = resources.getString(R.string.privacy_responses_account),
                isSwitch = false,
                click = { isActive -> showMessage("$isActive") }
            ))
            blockedAccounts.initialize(DrawableTextItem(
                startText = resources.getString(R.string.privacy_blocked_accounts),
                isSwitch = false,
                click = { isActive -> showMessage("$isActive") }
            ))
            mutedAccounts.initialize(DrawableTextItem(
                startText = resources.getString(R.string.privacy_muted_accounts),
                isSwitch = false,
                click = { isActive -> showMessage("$isActive") }
            ))
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