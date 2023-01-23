package com.vylo.signup.signupcreatepassword.presentation.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import com.vylo.common.BaseFragment
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ButtonType
import com.vylo.common.widget.MainInputType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpCreatePasswordBinding
import com.vylo.signup.signupcreatepassword.presentation.viewmodel.SignUpCreatePasswordFragmentViewModel
import com.vylo.signup.signupglobal.viewmodel.SignUpSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpCreatePasswordFragment : BaseFragment<FragmentSignUpCreatePasswordBinding>() {

    override fun getViewBinding() = FragmentSignUpCreatePasswordBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SignUpCreatePasswordFragmentViewModel>()
    private val sharedViewModel by sharedViewModel<SignUpSharedViewModel>()
    private var isShowPassword = false

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
    }

    private fun createContentOfView() {
        viewBinder.inputPassword.apply {
            val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
            initRectInput(
                R.string.label_password,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            )
            val shapeType = com.vylo.common.util.enums.InputType.INPUT_ROUND
            setInputShape(shapeType)
            showHideLine(View.VISIBLE)
            addTextChangedListenerValidation(this, ::checkInputValidation, iconOfValidation)
            setIconIntoInput(com.vylo.common.R.drawable.ic_baseline_visibility)
            val clickOnBaseline = View.OnClickListener {
                onShowHidePassword(this)
            }
            clickOnIconIntoInput(clickOnBaseline)
        }

        viewBinder.nextButton.apply {
            roundedGrayButtonStyle(requireContext(), resources.getString(R.string.label_next))
            clickOnButton { onNextClick() }
            setEnableOrDisabel(false)
        }

//        viewBinder.buttonRevealPassword.setOnClickListener { onShowHidePassword() }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity()) {}

    }

    private fun onShowHidePassword(mainInputType: MainInputType) {
        when(isShowPassword) {
            true -> {
                viewBinder.inputPassword.getInputType().inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                mainInputType.setIconIntoInput(com.vylo.common.R.drawable.ic_baseline_visibility)
                isShowPassword = false
            }
            false -> {
                viewBinder.inputPassword.getInputType().inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                mainInputType.setIconIntoInput(com.vylo.common.R.drawable.ic_baseline_visibility_off)
                isShowPassword = true
            }
        }
    }

    private fun onNextClick() {
        val signUpModel = sharedViewModel.getSignUp()?.copy(
            password = viewBinder.inputPassword.getInputText()
        )
        sharedViewModel.setSignUp(signUpModel)
        navigateTo(R.id.action_createPasswordFragment_to_signUpCreateUsernameFragment)
    }

    private fun checkInputValidation(view: MainInputType) =
        when (viewModel.passwordIsValid(view.getInputText())) {
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
}