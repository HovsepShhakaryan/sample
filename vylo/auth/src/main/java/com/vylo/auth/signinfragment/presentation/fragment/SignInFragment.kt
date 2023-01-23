package com.vylo.auth.signinfragment.presentation.fragment

import android.os.Bundle
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.vylo.auth.R
import com.vylo.auth.databinding.FragmentSigninBinding
import com.vylo.auth.signinfragment.presentation.viewmodel.SignInFragmentViewModel
import com.vylo.common.BaseFragment
import com.vylo.common.util.GoogleAnalytics
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ButtonType
import com.vylo.common.util.enums.ScreenType
import com.vylo.common.widget.MainInputType
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : BaseFragment<FragmentSigninBinding>() {

    private val viewModel by viewModel<SignInFragmentViewModel>()
    override fun getViewBinding() = FragmentSigninBinding.inflate(layoutInflater)
    private var isShowPassword = false

    companion object {

        @JvmStatic
        fun newInstance(): SignInFragment {
            return SignInFragment()
        }
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
        viewBinder.toolbar.setIconOfButtonBack(R.drawable.ic_circleback)
        val buttonBack = View.OnClickListener { requireActivity().onBackPressed() }
        viewBinder.toolbar.clickOnButtonBack(buttonBack)
    }

    private fun createContentOfView() {
        viewBinder.inputEmailAddress.initRectInput(
            R.string.label_email_hint,
            (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
        )

        viewBinder.inputPassword.initRectInput(
            R.string.label_password,
            (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        )
        viewBinder.inputPassword.setIconIntoInput(com.vylo.common.R.drawable.ic_baseline_visibility)
        val clickOnBaseline = View.OnClickListener {
            onShowHidePassword(viewBinder.inputPassword)
        }
        viewBinder.inputPassword.clickOnIconIntoInput(clickOnBaseline)

        viewBinder.buttonLogin.roundedWhiteButtonStyle(
            requireContext(),
            resources.getString(R.string.label_log_in_button),
            ButtonStyle.ROUNDED_BIG_MEDIUM
        )
        val buttonLogin = View.OnClickListener { checkFieldsValidation() }
        viewBinder.buttonLogin.clickOnButton(buttonLogin)

        viewBinder.linkButtonForgotPassword.setOnClickListener {
            navigateTo(com.vylo.resetpassword.R.id.reset_pass_navigation_graph)
        }


        val signUpLabel = SpannableString(resources.getString(R.string.label_new_to_column))
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                //TODO
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        val color = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary))
        signUpLabel.setSpan(clickableSpan, 15, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        signUpLabel.setSpan(color, 15, 27, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        viewModel.responseError.observe(viewLifecycleOwner) {
            hideProgress()
            showMessage(it)
        }
        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            sendAnalyticEvent(GoogleAnalytics.LOG_IN, GoogleAnalytics.LOGIN_MAIL)
            hideProgress()
            setScreenType(ScreenType.MAIN)
            showMessage(it)
            throwStartScreen()
            activity?.finish()
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

    private fun onBackClick() {
        Navigation.findNavController(viewBinder.root).popBackStack()
    }

    private fun checkFieldsValidation() {
        var isValidationPassed = true
        val emailAddressValidationResponse = viewModel.checkEmailAddressIsValid(viewBinder.inputEmailAddress.getInputText())
        val emailAddressEmptyResponse = viewModel.checkEmailAddressIsEmpty(viewBinder.inputEmailAddress.getInputText())
        val passwordValidationResponse = viewModel.checkPasswordIsValid(viewBinder.inputPassword.getInputText())
        val passwordEmptyResponse = viewModel.checkPasswordIsEmpty(viewBinder.inputPassword.getInputText())

        if (emailAddressEmptyResponse.isNotBlank()) {
            viewBinder.inputEmailAddress.setErrorTitle(emailAddressEmptyResponse)
            viewBinder.inputEmailAddress.setErrorTitleColor(ContextCompat.getColor(requireContext(), R.color.red))
            isValidationPassed = false
        } else if (emailAddressValidationResponse.isNotBlank()) {
            viewBinder.inputEmailAddress.setErrorTitle(emailAddressValidationResponse)
            viewBinder.inputEmailAddress.setErrorTitleColor(ContextCompat.getColor(requireContext(), R.color.red))
            isValidationPassed = false
        }
        if (passwordEmptyResponse.isNotBlank()) {
            viewBinder.inputPassword.setErrorTitle(passwordEmptyResponse)
            viewBinder.inputPassword.setErrorTitleColor(ContextCompat.getColor(requireContext(), R.color.red))
            isValidationPassed = false
        }

        if (isValidationPassed) {
            showProgress()
            viewModel.signInCall()
        }
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