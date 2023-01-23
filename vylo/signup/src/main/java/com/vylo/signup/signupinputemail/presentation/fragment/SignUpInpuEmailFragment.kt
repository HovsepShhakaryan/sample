package com.vylo.signup.signupinputemail.presentation.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.vylo.common.BaseFragment
import com.vylo.common.entity.SocialUserData
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ButtonType
import com.vylo.common.util.enums.TokenType
import com.vylo.common.widget.MainInputType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpInputEmailBinding
import com.vylo.signup.signupglobal.entity.SignUp
import com.vylo.signup.signupglobal.viewmodel.SignUpSharedViewModel
import com.vylo.signup.signupinputemail.presentation.viewmodel.SignUpWithEmailFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpWithEmailFragment : BaseFragment<FragmentSignUpInputEmailBinding>() {

    override fun getViewBinding() = FragmentSignUpInputEmailBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SignUpWithEmailFragmentViewModel>()
    private val sharedViewModel by sharedViewModel<SignUpSharedViewModel>()

    companion object {
        const val USER_DATA = "UserData"
        const val TOKEN_TYPE = "token_type"
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
        val userData = arguments?.get(USER_DATA)
        val tokenType = arguments?.get(TOKEN_TYPE)
        if (userData != null) {
            userData as SocialUserData
            sharedViewModel.setSocialUserData(userData)
            tokenType as TokenType
            sharedViewModel.setTokenType(tokenType)
            navigateTo(R.id.action_signUpWithEmailFragment_to_signUpGenderFragment)
        } else {
            createToolBar()
            createContentOfView()
        }
    }

    private fun createToolBar() {
        viewBinder.toolbar.setTitleImage(com.vylo.common.R.drawable.ic_vylo_name_new)
        viewBinder.toolbar.setIconOfButtonBack(R.drawable.ic_circleback)
        val buttonBack = View.OnClickListener { requireActivity().onBackPressed() }
        viewBinder.toolbar.clickOnButtonBack(buttonBack)
    }

    private fun createContentOfView() {
        viewBinder.inputEmail.apply {
            val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
            initRectInput(
                R.string.label_email_hint,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
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

        viewModel.responseError.observe(viewLifecycleOwner) {
            hideProgress()
            showMessage(it)
        }
        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            if (it != null && it) {
                val signUpModel = SignUp(email = viewBinder.inputEmail.getInputText())
                sharedViewModel.setSignUp(signUpModel)
                hideProgress()
                navigateTo(R.id.action_signUpWithEmailFragment_to_signUpVerificationCodeFragment)
            }
        }
    }

    private fun onNextClick() {
        showProgress()
        viewModel.checkEmailAddress(viewBinder.inputEmail.getInputText())
    }

    private fun checkInputValidation(view: MainInputType) =
        when (viewModel.emailIsValid(view.getInputText())) {
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
