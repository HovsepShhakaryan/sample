package com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.vylo.common.BaseFragment
import com.vylo.common.entity.RespondData
import com.vylo.common.ext.hideKeyboard
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.main.R
import com.vylo.main.databinding.FragmentUpdatePasswordBinding
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.domain.entity.request.PasswordRequest
import com.vylo.main.settingsprivacy.presentation.fragment.myaccount.presentation.updatepassword.presentation.viewmodel.UpdatePasswordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdatePasswordFragment : BaseFragment<FragmentUpdatePasswordBinding>() {

    private val viewModel by viewModel<UpdatePasswordViewModel>()

    override fun getViewBinding() = FragmentUpdatePasswordBinding.inflate(layoutInflater)

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
            setTitle(resources.getString(R.string.label_update_password))
            setStyleButtonBackText(com.vylo.common.R.style.MainText_H6_3)
            setNextBorderButtonStyle(com.vylo.common.R.drawable.shape_white_rect)
            setNextBorderButtonTextStyle(com.vylo.common.R.style.MainText_H8_4)
            setNextBorderButtonText(resources.getString(R.string.button_profile_save))
            clickOnButtonBorderNext { onSaveClick() }
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
        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            showMessage(it)
        }

        viewModel.responseError.observe(viewLifecycleOwner) {
            showMessage(it)
        }
    }

    private fun onSaveClick() {
        requireActivity().hideKeyboard()
        viewBinder.apply {
            focusWrapper.requestFocus()
            val inputError: String = if (currentPassword.getDesc().isEmpty()) {
                resources.getString(R.string.input_current_password)
            } else if (newPassword.getDesc().isEmpty()) {
                resources.getString(R.string.input_new_password)
            } else if (reNewPassword.getDesc().isEmpty()) {
                resources.getString(R.string.confirm_new_password)
            } else ""

            if (inputError.isNotEmpty()) {
                showMessage(inputError)
                return
            }

            PasswordRequest(
                newPassword = newPassword.getDesc(),
                reNewPassword = reNewPassword.getDesc(),
                currentPassword = currentPassword.getDesc(),
            ).let {
                viewModel.updatePassword(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().hideKeyboard()
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