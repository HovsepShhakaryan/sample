package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.requestverification.pesentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.vylo.common.BaseFragment
import com.vylo.main.R
import com.vylo.main.databinding.FragmentRequestVerificationBinding
import com.vylo.main.settingsprivacy.presentation.adapter.SettingsAdapter

class RequestVerificationFragment : BaseFragment<FragmentRequestVerificationBinding>() {

    override fun getViewBinding() = FragmentRequestVerificationBinding.inflate(layoutInflater)

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
            setTitle(resources.getString(R.string.request_verification))
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
//        val backButton = ContextCompat.getDrawable(requireContext(), R.drawable.ic_next_button)
//        val settingsList = listOf(
//            DrawableTextItem(
//                text = resources.getString(R.string.my_account),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_account),
//                endImage = backButton,
//                click = { navigateTo(R.id.toMyAccountFragment) }
//            ),
//            DrawableTextItem(
//                text = resources.getString(R.string.follow_invite_friends),
//                startImage = ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_follow_invite_friends
//                ),
//                endImage = backButton,
//                click = { navigateTo(R.id.toFollowInviteFriendsFragment) }
//            ),
//            DrawableTextItem(
//                text = resources.getString(R.string.push_notifications),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_bell),
//                endImage = backButton,
//                click = { navigateTo(R.id.toPushNotificationsFragment) }
//            ),
//            DrawableTextItem(
//                text = resources.getString(R.string.email_settings),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_email),
//                endImage = backButton,
//                click = { navigateTo(R.id.toEmailSettingsFragment) }
//            ),
//            DrawableTextItem(
//                text = resources.getString(R.string.privacy),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_privacy),
//                endImage = backButton,
//                click = { navigateTo(R.id.toPrivacyFragment) }
//            ),
//            DrawableTextItem(
//                text = resources.getString(R.string.security),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_security),
//                endImage = backButton,
//                click = { navigateTo(R.id.toSecurityFragment) }
//            ),
//            DrawableTextItem(
//                text = resources.getString(R.string.connected_account),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_link),
//                endImage = backButton,
//                click = { navigateTo(R.id.toConnectedAccountsFragment) }
//            ),
//            DrawableTextItem(
//                text = resources.getString(R.string.help),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_help),
//                endImage = backButton,
//                click = { showMessage("Help") }
//            ),
//            DrawableTextItem(
//                text = resources.getString(R.string.about),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_about),
//                endImage = backButton,
//                click = { navigateTo(R.id.toAboutFragment) }
//            )
//        )
//
//        adapter.submitList(settingsList)
//        viewBinder.apply {
//            list.adapter = adapter
//            list.addItemDecoration(
//                MarginItemDecoration(
//                    bottom = resources.getDimensionPixelSize(
//                        R.dimen.margin_padding_size_large_mid
//                    )
//                )
//            )
//        }
    }
}