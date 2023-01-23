package com.vylo.signup.signupcomplete.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.vylo.common.BaseFragment
import com.vylo.common.ext.createLinks
import com.vylo.common.ext.generateRandomPassword
import com.vylo.common.util.*
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.TokenType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpCompleteBinding
import com.vylo.signup.signupcomplete.domain.entity.SignIn
import com.vylo.signup.signupcomplete.domain.entity.UserData
import com.vylo.signup.signupcomplete.presentation.viewmodel.SignUpCompleteFragmentViewModel
import com.vylo.signup.signupglobal.viewmodel.SignUpSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpCompleteFragment : BaseFragment<FragmentSignUpCompleteBinding>() {

    override fun getViewBinding() = FragmentSignUpCompleteBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SignUpCompleteFragmentViewModel>()
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
        createContentOfView()
    }

    private fun createContentOfView() {
        viewBinder.signUpButton.apply {
            roundedWhiteButtonStyle(
                requireContext(),
                resources.getString(R.string.label_sign_up),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            clickOnButton { onSignUpClick() }
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
            if (it != null) {
                val signUpModel = sharedViewModel.getSignUp()
                val signInModel = SignIn(
                    signUpModel?.email,
                    signUpModel?.password
                )
                viewModel.signIn(signInModel)
            }
        }

        viewModel.responseSuccessSignIn.observe(viewLifecycleOwner) {
            sendAnalyticEvent(GoogleAnalytics.SIGN_UP, GoogleAnalytics.SIGN_UP_MAIL)
            hideProgress()
            navigateTo(R.id.action_signUpCompleteFragment_to_signUpPickUpProfilePictureFragment)
        }

        viewModel.responseUpdateSuccess.observe(viewLifecycleOwner) {
            hideProgress()
            navigateTo(R.id.action_signUpCompleteFragment_to_signUpPickUpProfilePictureFragment)
        }
    }

    private fun onTermsClick() {
        val bundle = Bundle()
        bundle.putBoolean(OPEN_WEB_FROM, false)
        bundle.putString(WEB_DATA, TERMS)
        navigateTo(
            R.id.action_signUpCompleteFragment_to_signUpWebViewFragment,
            bundle
        )
    }

    private fun onLearnMoreClick() {
        //TODO some action
    }

    private fun onCookieUseClick() {
        //TODO some action
    }

    private fun onPrivacyClick() {
        val bundle = Bundle()
        bundle.putBoolean(OPEN_WEB_FROM, false)
        bundle.putString(WEB_DATA, PRIVACY)
        navigateTo(
            R.id.action_signUpCompleteFragment_to_signUpWebViewFragment,
            bundle
        )
    }

    private fun onSignUpClick() {
        showProgress()
        val signUpModel = sharedViewModel.getSignUp()
        val tokenType = sharedViewModel.getTokenType()
        if (sharedViewModel.getSocialUserData() != null) {
            if (signUpModel != null) {
                val userData = UserData(
                    signUpModel.username,
                    signUpModel.name,
                    null,
                    null,
                    null,
                    signUpModel.gender,
                    signUpModel.birthday_date,
                    "q3!Q${generateRandomPassword()}"
                )
                when (tokenType) {
                    TokenType.FACEBOOK -> viewModel.signInFacebook(userData)
                    TokenType.GOOGLE -> viewModel.signInGoogle(userData)
                    else -> {}
                }
            }
        } else
            viewModel.signUp(signUpModel!!)


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