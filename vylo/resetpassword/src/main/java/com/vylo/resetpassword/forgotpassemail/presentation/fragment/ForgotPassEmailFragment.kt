package com.vylo.resetpassword.forgotpassemail.presentation.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vylo.common.BaseFragment
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.widget.MainInputType
import com.vylo.resetpassword.R
import com.vylo.resetpassword.databinding.FragmentForgotPassEmailBinding
import com.vylo.resetpassword.forgotpassemail.presentation.viewmodel.ForgotPassEmailFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ForgotPassEmailFragment : BaseFragment<FragmentForgotPassEmailBinding>() {

    override fun getViewBinding() = FragmentForgotPassEmailBinding.inflate(layoutInflater)
    private val viewModel by viewModel<ForgotPassEmailFragmentViewModel>()

    companion object {
        const val TOKEN = "token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
        createToolBar()
        createContentOfView()
    }

    private fun createToolBar() {
        viewBinder.toolbar.setTitleImage(com.vylo.common.R.drawable.ic_vylo_name_new)
        viewBinder.toolbar.setIconOfButtonBack(R.drawable.ic_circleback)
        val buttonBack = View.OnClickListener { requireActivity().onBackPressed() }
        viewBinder.toolbar.clickOnButtonBack(buttonBack)
    }

    private fun createContentOfView() {
        viewBinder.inputEmailAddress.apply {
            val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
            initRectInput(
                R.string.label_new_email_hint,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            )
            hideInputTitle(View.VISIBLE)
            setInputTitle(resources.getString(R.string.label_new_email_hint))
            setInputTitleColor(ContextCompat.getColor(requireContext(),com.vylo.common.R.color.primary))
            val shapeType = com.vylo.common.util.enums.InputType.INPUT_RECT
            setInputShape(shapeType)
            showHideLine(View.GONE)
            addTextChangedListenerValidation(this, ::checkInputValidation, iconOfValidation)
        }

        viewBinder.buttonSend.apply {
            roundedGrayButtonStyle(requireContext(), resources.getString(R.string.label_send))
            clickOnButton { onNextClick() }
            setEnableOrDisabel(false)
        }

        viewModel.responseError.observe(viewLifecycleOwner) {
            hideProgress()
            showMessage(it)
        }

        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            if (it != null) {
                hideProgress()
                showDialog(it.token!!)
            }
        }
    }

    private fun checkInputValidation(view: MainInputType) =
        when (viewModel.emailIsValid(view.getInputText())) {
            "" -> {
                viewBinder.buttonSend.setEnableOrDisabel(true)
                viewBinder.buttonSend.roundedWhiteButtonStyle(
                    requireContext(),
                    resources.getString(R.string.label_send),
                    ButtonStyle.ROUNDED_BIG_MEDIUM
                )
                true
            }
            else -> {
                viewBinder.buttonSend.setEnableOrDisabel(false)
                viewBinder.buttonSend.roundedGrayButtonStyle(requireContext(), resources.getString(R.string.label_send))
                false
            }
        }

    private fun onNextClick() {
        showProgress()
        viewModel.sendEmail(viewBinder.inputEmailAddress.getInputText())
    }

    override fun showProgress() {
        super.showProgress()
        viewBinder.progressBar.show()
    }

    override fun hideProgress() {
        super.hideProgress()
        viewBinder.progressBar.hide()
    }

    private fun showDialog(token: String) {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setCancelable(false)
            .setMessage(resources.getString(R.string.label_dialog_success_info))
            .setPositiveButton(resources.getString(R.string.label_ok)
            ) { dialog1, _ ->
                dialog1.dismiss()
                navigateTo(
                    R.id.action_forgotPassEmailFragment_to_forgotPassCodeFragment,
                    bundleOf(
                        TOKEN to token,
                    )
                )
            }
            .create()
            .show()
    }
}