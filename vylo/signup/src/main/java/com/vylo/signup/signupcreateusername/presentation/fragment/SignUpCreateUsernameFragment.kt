package com.vylo.signup.signupcreateusername.presentation.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.vylo.common.BaseFragment
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ButtonType
import com.vylo.common.widget.MainInputType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpCreateUsernameBinding
import com.vylo.signup.signupcreateusername.presentation.viewmodel.SignUpCreateUsernameFragmentViewModel
import com.vylo.signup.signupglobal.viewmodel.SignUpSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpCreateUsernameFragment : BaseFragment<FragmentSignUpCreateUsernameBinding>() {

    override fun getViewBinding() = FragmentSignUpCreateUsernameBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SignUpCreateUsernameFragmentViewModel>()
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
    }

    private fun createContentOfView() {
        viewBinder.inputUsername.apply {
            val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
            initRectInput(
                R.string.label_username,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_TEXT)
            )
            val shapeType = com.vylo.common.util.enums.InputType.INPUT_ROUND
            setInputShape(shapeType)
            showHideLine(View.VISIBLE)
            setInputTypeTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
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
                val signUpModel = sharedViewModel.getSignUp()?.copy(
                    username = viewBinder.inputUsername.getInputText()
                )
                sharedViewModel.setSignUp(signUpModel)
                hideProgress()
                navigateTo(R.id.action_signUpCreateUsernameFragment_to_signUpCompleteFragment)
            }
        }
    }

    private fun onNextClick() {
        showProgress()
        viewModel.checkUsername(viewBinder.inputUsername.getInputText())
    }

    private fun checkInputValidation(view: MainInputType) =
        when (val result = viewModel.usernameValidation(view.getInputText())) {
            "" -> {
                viewBinder.inputUsername.setErrorTitle(result)
                viewBinder.nextButton.setEnableOrDisabel(true)
                viewBinder.nextButton.roundedWhiteButtonStyle(
                    requireContext(),
                    resources.getString(R.string.label_next),
                    ButtonStyle.ROUNDED_BIG_MEDIUM
                )
                true
            }
            else -> {
                viewBinder.inputUsername.getInputType().setSelection(viewBinder.inputUsername.getInputType().length())
                viewBinder.nextButton.setEnableOrDisabel(false)
                viewBinder.nextButton.roundedWhiteButtonStyle(
                    requireContext(),
                    resources.getString(R.string.label_next),
                    ButtonStyle.ROUNDED_BIG_MEDIUM
                )
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