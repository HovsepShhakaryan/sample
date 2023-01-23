package com.vylo.resetpassword.forgotpasscode.presentation.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.vylo.common.BaseFragment
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ButtonType
import com.vylo.common.util.enums.ScreenType
import com.vylo.common.widget.MainInputType
import com.vylo.resetpassword.R
import com.vylo.resetpassword.databinding.FragmentForgotPassCodeBinding
import com.vylo.resetpassword.forgotpasscode.presentation.viewmodel.ForgotPassCodeFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPassCodeFragment : BaseFragment<FragmentForgotPassCodeBinding>() {

    override fun getViewBinding() = FragmentForgotPassCodeBinding.inflate(layoutInflater)
    private val viewModel by viewModel<ForgotPassCodeFragmentViewModel>()
    private var isNewPasswordEmpty: Boolean = false
    private var isConfirmPasswordEmpty: Boolean = false
    private var isCodeEmpty: Boolean = false
    private var token: String? = null
    private var isShowPassword = false

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

        token = arguments?.getString(TOKEN)

        viewBinder.inputCode.apply {
            val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
            initRectInput(
                R.string.label_enter_your_code,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            )
            hideInputTitle(View.VISIBLE)
            setInputTitle(resources.getString(R.string.label_enter_your_code))
            setInputTitleColor(ContextCompat.getColor(requireContext(), com.vylo.common.R.color.secondary))
            val shapeType = com.vylo.common.util.enums.InputType.INPUT_RECT
            setInputShape(shapeType)
            showHideLine(View.GONE)
            addTextChangedListenerValidation(this, ::checkInputValidationCode, iconOfValidation)
        }

        viewBinder.inputNewPassword.apply {
            val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
            initRectInput(
                R.string.label_new_password,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            )
            hideInputTitle(View.VISIBLE)
            setInputTitle(resources.getString(R.string.label_new_password))
            setInputTitleColor(ContextCompat.getColor(requireContext(), com.vylo.common.R.color.secondary))
            val shapeType = com.vylo.common.util.enums.InputType.INPUT_RECT
            setInputShape(shapeType)
            showHideLine(View.GONE)
            addTextChangedListenerValidation(this, ::checkInputValidationNewPass, iconOfValidation)
            setIconIntoInput(com.vylo.common.R.drawable.ic_baseline_visibility)
            val clickOnBaseline = View.OnClickListener {
                onShowHidePassword(this)
            }
            clickOnIconIntoInput(clickOnBaseline)
        }

        viewBinder.inputConfirmNewPassword.apply {
            val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
            initRectInput(
                R.string.label_confirm_new_password,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            )
            hideInputTitle(View.VISIBLE)
            setInputTitle(resources.getString(R.string.label_confirm_new_password))
            setInputTitleColor(ContextCompat.getColor(requireContext(), com.vylo.common.R.color.secondary))
            val shapeType = com.vylo.common.util.enums.InputType.INPUT_RECT
            setInputShape(shapeType)
            showHideLine(View.GONE)
            addTextChangedListenerValidation(this, ::checkInputValidationConfirmNewPass, iconOfValidation)
            setIconIntoInput(com.vylo.common.R.drawable.ic_baseline_visibility)
            val clickOnBaseline = View.OnClickListener {
                onShowHidePassword(this)
            }
            clickOnIconIntoInput(clickOnBaseline)
        }

        viewBinder.buttonSubmit.apply {
            roundedWhiteButtonStyle(
                requireContext(),
                resources.getString(R.string.label_submit),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            clickOnButton { onNextClick() }
//            setEnableOrDisabel(false)
        }

        viewModel.responseError.observe(viewLifecycleOwner) {
            hideProgress()
            if (it != null) showMessage(it)
        }

        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            if (it != null) {
                hideProgress()
                showMessage(it)
                setScreenType(ScreenType.AUTH)
                setIsResetPass(true)
                throwStartScreen()
                activity?.finish()
            }
        }
    }

    private fun onShowHidePassword(mainInputType: MainInputType) {
        when(isShowPassword) {
            true -> {
                mainInputType.getInputType().inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                mainInputType.setIconIntoInput(com.vylo.common.R.drawable.ic_baseline_visibility)
                isShowPassword = false
            }
            false -> {
                mainInputType.getInputType().inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                mainInputType.setIconIntoInput(com.vylo.common.R.drawable.ic_baseline_visibility_off)
                isShowPassword = true
            }
        }
    }

    private fun checkInputValidationCode(view: MainInputType) =
        when (viewModel.codeIsNotEmpty(view.getInputText())) {
            "" -> {
                isCodeEmpty = true
//                activateNextButton()
                true
            }
            else -> {
                isCodeEmpty = false
//                activateNextButton()
                false
            }
        }

    private fun checkInputValidationNewPass(view: MainInputType) =
        when (viewModel.passwordIsValid(view.getInputText())) {
            "" -> {
                val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
                isNewPasswordEmpty = true
//                activateNextButton()
                true
            }
            else -> {
                isNewPasswordEmpty = false
//                activateNextButton()
                false
            }
        }

    private fun checkInputValidationConfirmNewPass(view: MainInputType) =
        when (viewModel.passwordIsValid(view.getInputText())) {
            "" -> {
                isConfirmPasswordEmpty = true
//                activateNextButton()
                true
            }
            else -> {
                isConfirmPasswordEmpty = false
//                activateNextButton()
                false
            }
        }

    private fun activateNextButton() {
        if (isCodeEmpty && isNewPasswordEmpty && isConfirmPasswordEmpty) {
            viewBinder.buttonSubmit.setEnableOrDisabel(true)
            viewBinder.buttonSubmit.setTitleColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.main
                )
            )
            viewBinder.buttonSubmit.setButtonColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.secondary
                )
            )
        } else {
            viewBinder.buttonSubmit.setEnableOrDisabel(false)
            viewBinder.buttonSubmit.setTitleColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.white
                )
            )
            viewBinder.buttonSubmit.setButtonColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.black_disable
                )
            )
        }
    }

    private fun onNextClick() {
        showProgress()
        viewModel.getCode(viewBinder.inputCode.getInputText())
        viewModel.getPassword(viewBinder.inputNewPassword.getInputText())
        viewModel.getConfirmPassword(viewBinder.inputConfirmNewPassword.getInputText())
        if (token != null) viewModel.sendConfirmation(token!!)
    }

    override fun showProgress() {
        super.showProgress()
        viewBinder.progressBar.show()
    }

    override fun hideProgress() {
        super.hideProgress()
        viewBinder.progressBar.hide()
    }
}