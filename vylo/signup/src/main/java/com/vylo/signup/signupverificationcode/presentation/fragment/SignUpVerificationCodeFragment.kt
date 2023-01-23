package com.vylo.signup.signupverificationcode.presentation.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.vylo.common.BaseFragment
import com.vylo.common.ext.createLinks
import com.vylo.common.util.OPEN_WEB_FROM
import com.vylo.common.util.PRIVACY
import com.vylo.common.util.TERMS
import com.vylo.common.util.WEB_DATA
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ButtonType
import com.vylo.common.widget.MainInputType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpVerificationCodeBinding
import com.vylo.signup.signupglobal.viewmodel.SignUpSharedViewModel
import com.vylo.signup.signupverificationcode.presentation.viewmodel.SignUpVerificationCodeFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpVerificationCodeFragment : BaseFragment<FragmentSignUpVerificationCodeBinding>() {

    override fun getViewBinding() = FragmentSignUpVerificationCodeBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SignUpVerificationCodeFragmentViewModel>()
    private val sharedViewModel by sharedViewModel<SignUpSharedViewModel>()

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
        val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
        viewBinder.inputCode.apply {
            initRectInput(
                R.string.label_code,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
            )
            val shapeType = com.vylo.common.util.enums.InputType.INPUT_ROUND
            setInputShape(shapeType)
            showHideLine(View.VISIBLE)
            addTextChangedListenerValidation(this, ::checkInputValidation, iconOfValidation)
        }

        viewBinder.nextButton.apply {
            roundedGrayButtonStyle(requireContext(), resources.getString(R.string.label_next))
            clickOnButton { onNextClick() }
            setEnableOrDisabel(false)
        }

        viewBinder.descriptionText.createLinks(
            R.string.label_privacy_policy,
            ContextCompat.getColor(requireContext(), R.color.primary)
        ) { onPrivacyClick() }

        viewBinder.descriptionText.createLinks(
            R.string.label_terms,
            ContextCompat.getColor(requireContext(), R.color.primary)
        ) { onTermsClick() }

        viewModel.responseError.observe(viewLifecycleOwner) {
            hideProgress()
            showMessage(it)
        }

        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            if (it != null && it) {
                hideProgress()
                navigateTo(
                    R.id.action_signUpVerificationCodeFragment_to_signUpGenderFragment
                )
            }
        }
    }

    private fun onTermsClick() {
        val bundle = Bundle()
        bundle.putBoolean(OPEN_WEB_FROM, true)
        bundle.putString(WEB_DATA, TERMS)
        navigateTo(
            R.id.action_signUpVerificationCodeFragment_to_signUpWebViewFragment,
            bundle
        )
    }

    private fun onPrivacyClick() {
        val bundle = Bundle()
        bundle.putBoolean(OPEN_WEB_FROM, true)
        bundle.putString(WEB_DATA, PRIVACY)
        navigateTo(
            R.id.action_signUpVerificationCodeFragment_to_signUpWebViewFragment,
            bundle
        )
    }

    private fun onNextClick() {
        if (sharedViewModel.getSignUp() != null) {
            showProgress()
            val emailForVerification = sharedViewModel.getSignUp()!!.email
            viewModel.sendVerificationCode(
                viewBinder.inputCode.getInputText(),
                emailForVerification!!
            )
        }
    }

    private fun checkInputValidation(view: MainInputType) =
        when (viewModel.codeIsEmpty(view.getInputText())) {
            "" -> {
                viewBinder.nextButton.roundedWhiteButtonStyle(
                    requireContext(),
                    resources.getString(R.string.label_next),
                    ButtonStyle.ROUNDED_BIG_MEDIUM
                )
                viewBinder.nextButton.setEnableOrDisabel(true)
                true
            }
            else -> {
                viewBinder.nextButton.roundedGrayButtonStyle(requireContext(), resources.getString(R.string.label_next))
                viewBinder.nextButton.setEnableOrDisabel(false)
                false
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