package com.vylo.main.settingsprivacy.presentation.fragment

import android.content.Context
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
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentSettingsPrivacyBinding
import com.vylo.main.settingsprivacy.presentation.adapter.SettingsAdapter

class SettingsPrivacyFragment : BaseFragment<FragmentSettingsPrivacyBinding>() {

    override fun getViewBinding() = FragmentSettingsPrivacyBinding.inflate(layoutInflater)
    private lateinit var activity: MainFlawActivity

    private val adapter: SettingsAdapter by lazy {
        SettingsAdapter()
    }

    override fun onAttach(context: Context) {
        activity = getActivity() as MainFlawActivity
        activity.hideNavBar()
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onResume() {
        super.onResume()
        activity.showNavBar()
    }

    private fun beginning() {
        createToolbar()
        createContentOfView()
    }

    private fun createToolbar() {
        viewBinder.toolbar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack { backToPrevious() }
            setTitle(resources.getString(R.string.label_settings_and_privacy))
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
                startText = resources.getString(R.string.my_account),
                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_account),
                endImage = backButton,
                click = { navigateTo(R.id.toMyAccountFragment) }
            ),
//            DrawableTextItem(
//                startText = resources.getString(R.string.follow_invite_friends),
//                startImage = ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_follow_invite_friends
//                ),
//                endImage = backButton,
//                click = { navigateTo(R.id.toFollowInviteFriendsFragment) }
//            ),
//            DrawableTextItem(
//                startText = resources.getString(R.string.push_notifications),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_bell),
//                endImage = backButton,
//                click = { navigateTo(R.id.toPushNotificationsFragment) }
//            ),
//            DrawableTextItem(
//                startText = resources.getString(R.string.email_settings),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_email),
//                endImage = backButton,
//                click = { navigateTo(R.id.toEmailSettingsFragment) }
//            ),
//            DrawableTextItem(
//                startText = resources.getString(R.string.privacy),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_privacy),
//                endImage = backButton,
//                click = { navigateTo(R.id.toPrivacyFragment) }
//            ),
//            DrawableTextItem(
//                startText = resources.getString(R.string.security),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_security),
//                endImage = backButton,
//                click = { navigateTo(R.id.toSecurityFragment) }
//            ),
//            DrawableTextItem(
//                startText = resources.getString(R.string.connected_account),
//                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_link),
//                endImage = backButton,
//                click = { navigateTo(R.id.toConnectedAccountsFragment) }
//            ),
            DrawableTextItem(
                startText = resources.getString(R.string.help),
                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_help),
                endImage = backButton,
                click = {
                    navigateTo(
                        R.id.toWebFragment, bundleOf(
                            WEB_DATA to WebData(url = "https://vylo.com/faq")
                        )
                    )
                }
            ),
            DrawableTextItem(
                startText = resources.getString(R.string.about),
                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_about),
                endImage = backButton,
                click = { navigateTo(R.id.toAboutFragment) }
            )
        )

        adapter.submitList(settingsList)
        viewBinder.apply {
            list.adapter = adapter
            list.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        R.dimen.margin_padding_size_large_mid
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