package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.adapter.entity.DrawableTextItem
import com.vylo.common.entity.RespondData
import com.vylo.common.util.PARENT_FRAGMENT
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.common.util.enums.ScreenType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentMyAccountBinding
import com.vylo.main.settingsprivacy.presentation.adapter.SettingsAdapter
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.viewmodel.MyAccountViewModel
import com.vylo.main.settingsprivacy.presentation.viewmodel.SettingsSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyAccountFragment : BaseFragment<FragmentMyAccountBinding>() {

    private val settingsViewModel by sharedViewModel<SettingsSharedViewModel>()
    private val viewModel by viewModel<MyAccountViewModel>()
    override fun getViewBinding() = FragmentMyAccountBinding.inflate(layoutInflater)
    private lateinit var activity: MainFlawActivity

    private val adapter: SettingsAdapter by lazy {
        SettingsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as MainFlawActivity
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
            setTitle(resources.getString(R.string.my_account))
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
                startText = resources.getString(R.string.personal_information),
                endImage = backButton,
                click = {
                    navigateTo(
                        R.id.toPersonalInformationFragment, bundleOf(
                            PARENT_FRAGMENT to simpleName
                        )
                    )
                }
            ),
            DrawableTextItem(
                startText = resources.getString(R.string.change_my_password),
                endImage = backButton,
                click = {
                    navigateTo(
                        R.id.toUpdatePasswordFragment, bundleOf(
                            PARENT_FRAGMENT to simpleName
                        )
                    )
                }
            ),
//            DrawableTextItem(
//                startText = resources.getString(R.string.request_verification),
//                endImage = backButton,
//                click = { showMessage("Request verification") }
//            ),
            DrawableTextItem(
                startText = resources.getString(R.string.delete_account),
                endImage = backButton,
                click = {
                    navigateTo(
                        R.id.toDeleteAccountFragment, bundleOf(
                            PARENT_FRAGMENT to simpleName
                        )
                    )
                }
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

            logout.setOnClickListener {
                LogoutDialogFragment(settingsViewModel.getProfileInfo()?.email.orEmpty()) {
                    viewModel.removeToken()
                    viewModel.removeMyGlobalId()
                    setScreenType(ScreenType.AUTH)
                    showMessage("Logged out")
                    throwStartScreen()
                    activity.finish()
                }.show(requireActivity().supportFragmentManager, null)
            }
        }
    }

    companion object {
        val simpleName: String
            get() = MyAccountFragment().javaClass.simpleName
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