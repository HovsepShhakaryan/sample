package com.vylo.main.settingsprivacy.presentation.fragment.followinvitefriends.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.adapter.entity.DrawableTextItem
import com.vylo.main.R
import com.vylo.main.databinding.FragmentFollowInviteFriendsBinding
import com.vylo.main.settingsprivacy.presentation.adapter.SettingsAdapter

class FollowInviteFriendsFragment : BaseFragment<FragmentFollowInviteFriendsBinding>() {

    override fun getViewBinding() = FragmentFollowInviteFriendsBinding.inflate(layoutInflater)

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
            setTitle(resources.getString(R.string.follow_invite_friends))
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
        val settingsList = listOf(
            DrawableTextItem(
                startText = resources.getString(R.string.invite_friends_by_whats_app),
                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_whatsapp),
                click = { showMessage("WhatsApp") }
            ),
            DrawableTextItem(
                startText = resources.getString(R.string.invite_friends_by_sms),
                startImage = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_sms
                ),
                click = { showMessage("SMS") }
            ),
            DrawableTextItem(
                startText = resources.getString(R.string.invite_friends_by_email),
                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_email),
                click = { showMessage("Email") }
            ),
            DrawableTextItem(
                startText = resources.getString(R.string.invite_friends_by_link),
                startImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_invite_link),
                click = { showMessage("Link") }
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
}