package com.vylo.main.settingsprivacy.presentation.fragment.connectedaccounts.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.adapter.entity.DrawableTextItem
import com.vylo.common.entity.RespondData
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.main.R
import com.vylo.main.databinding.FragmentConnectedAccountsBinding
import com.vylo.main.settingsprivacy.presentation.adapter.SettingsAdapter
import com.vylo.main.settingsprivacy.presentation.viewmodel.SettingsSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConnectedAccountsFragment : BaseFragment<FragmentConnectedAccountsBinding>() {

    private val settingsViewModel by sharedViewModel<SettingsSharedViewModel>()
    override fun getViewBinding() = FragmentConnectedAccountsBinding.inflate(layoutInflater)

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
            setTitle(resources.getString(R.string.connected_account))
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
        val profileInfo = settingsViewModel.getProfileInfo()
        val nextButton = ContextCompat.getDrawable(requireContext(), R.drawable.ic_next_button)
        val settingsList = listOf(
            DrawableTextItem(
                startText = resources.getString(R.string.connected_account_facebook),
                endText = profileInfo?.name.orEmpty(),
                endTextColor = ContextCompat.getColor(requireContext(), R.color.primary),
                startImage = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_privacy_facebook
                ),
                endImage = nextButton,
                click = { showMessage("facebook") }
            ),
            DrawableTextItem(
                startText = resources.getString(R.string.connected_account_google),
                startTextColor = ContextCompat.getColor(requireContext(), R.color.white_grey_text),
                startImage = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_privacy_google
                ),
                endImage = nextButton,
                click = { showMessage("google") }
            ),
            DrawableTextItem(
                startText = resources.getString(R.string.connected_account_email),
                endText = profileInfo?.email.orEmpty(),
                endTextColor = ContextCompat.getColor(requireContext(), R.color.primary),
                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_email),
                endImage = nextButton,
                click = { showMessage("email") }
            )
        )

        adapter.submitList(settingsList)
        viewBinder.apply {
            list.adapter = adapter
            list.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        R.dimen.margin_padding_size_large_small_mid
                    )
                )
            )
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