package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.vylo.common.BaseFragment
import com.vylo.common.dialog.SimpleDialogFragment
import com.vylo.common.entity.RespondData
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.common.util.enums.ScreenType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.databinding.FragmentDeleteAccountBinding
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.deleteaccount.presentation.viewmodel.DeleteAccountViewModel
import com.vylo.main.settingsprivacy.presentation.viewmodel.SettingsSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DeleteAccountFragment : BaseFragment<FragmentDeleteAccountBinding>() {

    private val viewModel by viewModel<DeleteAccountViewModel>()
    private val settingsViewModel by sharedViewModel<SettingsSharedViewModel>()

    override fun getViewBinding() = FragmentDeleteAccountBinding.inflate(layoutInflater)
    private lateinit var activity: MainFlawActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainFlawActivity
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
            setTitle(resources.getString(R.string.label_delete_account))
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
        viewModel.responseError.observe(viewLifecycleOwner) {
            showMessage(it)
        }

        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            setScreenType(ScreenType.AUTH)
            showMessage(it)
            throwStartScreen()
            activity.finish()
        }

        viewBinder.deleteAccount.setOnClickListener {
            SimpleDialogFragment(
                title = settingsViewModel.getProfileInfo()?.getUserName().orEmpty(),
                message = resources.getString(R.string.delete_account_question),
                okButtonTitle = resources.getString(R.string.delete_dialog_button),
                cancelButtonTitle = resources.getString(R.string.dialog_cancel),
                onOkClick = {
                    viewModel.deleteAccount()
                }
            )
                .show(requireActivity().supportFragmentManager, null)
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