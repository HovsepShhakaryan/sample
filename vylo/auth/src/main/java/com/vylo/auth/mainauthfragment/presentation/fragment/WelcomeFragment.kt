package com.vylo.auth.mainauthfragment.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.vylo.auth.R
import com.vylo.auth.activity.AuthActivity
import com.vylo.auth.databinding.FragmentWelcomeBinding
import com.vylo.auth.mainauthfragment.presentation.viewmodel.WelcomeFragmentViewModel
import com.vylo.common.BaseFragment
import com.vylo.common.api.Config.GOOGLE_CLIENT_ID
import com.vylo.common.entity.SocialUserData
import com.vylo.common.ext.createLinks
import com.vylo.common.ext.makeDefaultTheme
import com.vylo.common.ext.makeStatusBarTransparent
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ButtonType
import com.vylo.common.util.enums.ScreenType
import com.vylo.common.util.enums.TokenType
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    override fun getViewBinding() = FragmentWelcomeBinding.inflate(layoutInflater)
    private val viewModel by viewModel<WelcomeFragmentViewModel>()
    private lateinit var googleAuthActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var callbackManager: CallbackManager
    private lateinit var activity: AuthActivity


    companion object {
        const val PUBLIC_PROFILE = "public_profile"
        const val USER_DATA = "UserData"
        const val FIELDS = "fields"
        const val PROFILE_DATA = "id,name"
        const val TOKEN_TYPE = "token_type"
    }

    override fun onAttach(context: Context) {
        activity = getActivity() as AuthActivity
        super.onAttach(context)
        googleAuthActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            task.addOnCompleteListener {
                if (task.isSuccessful) handleSignInResult(task)
                else showMessage(resources.getString(R.string.label_social_error))
            }
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val accessToken = account.idToken

            if (account != null) {
                val name = account.displayName
                val imageUrl = account.photoUrl.toString()
                activity.showProgress()
                viewModel.sendSocialToken(accessToken!!, TokenType.GOOGLE)

                viewModel.responseSuccess.observe(viewLifecycleOwner) {
                    if (it != null) {
                        activity.hideProgress()
                        navigateTo(
                            com.vylo.signup.R.id.signup_nav_graph,
                            bundleOf(
                                USER_DATA to SocialUserData(
                                    name,
                                    imageUrl
                                ),
                                TOKEN_TYPE to TokenType.GOOGLE
                            )
                        )
                    }
                }
            }

        } catch (e: ApiException) {
            showMessage("${e.statusCode}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onStart() {
        requireActivity().makeStatusBarTransparent()
        super.onStart()
    }

    private fun beginning() {
        if (getIsResetPass()) {
            setIsResetPass(false)
            onLogInClick()
        } else createContentOfView()
    }

    private fun createContentOfView() {
        viewBinder.apply {

            // email
            buttonEmail.apply {
                roundedWhiteButtonStyle(
                    requireContext(),
                    resources.getString(R.string.label_continue_with_email),
                    ButtonStyle.ROUNDED_BIG_MEDIUM
                )
                clickOnButton { onEmailClick() }
            }

//             facebook
            buttonFacebook.apply {
                setButtonShape(ButtonType.BUTTON_GRAY_ROUND)
                setTitle(resources.getString(R.string.label_continue_with_facebook))
                setButtonColor(ContextCompat.getColor(requireContext(), R.color.main))
                setIcon(com.vylo.common.R.drawable.ic_facebook)
                clickOnButton { onFacebookClick() }
            }

            // google
            buttonGoogle.apply {
                setButtonShape(ButtonType.BUTTON_GRAY_ROUND)
                setTitle(resources.getString(R.string.label_continue_with_google))
                setButtonColor(ContextCompat.getColor(requireContext(), R.color.main))
                setIcon(com.vylo.common.R.drawable.ic_google)
                clickOnButton { onGoogleClick() }
            }

//             log in
//            linkButtonLogIn.createLinks(R.string.label_log_in) { onLogInClick() }
            linkButtonLogIn.setOnClickListener { onLogInClick() }

            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        val accessToken = result.accessToken.token
                        val request = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken()
                        ) { `object`, _ ->

                            activity.showProgress()
                            viewModel.sendSocialToken(accessToken, TokenType.FACEBOOK)

                            viewModel.responseSuccess.observe(viewLifecycleOwner) {
                                if (it != null) {
                                    if (`object` != null) {
                                        val id = `object`.getString("id")
                                        val name = `object`.getString("name")
                                        val imageUrl = "http://graph.facebook.com/$id/picture?type=large"

                                        val userData = Bundle()
                                        userData.putParcelable(
                                            USER_DATA,
                                            SocialUserData(
                                                name,
                                                imageUrl
                                            ),
                                        )
                                        navigateTo(
                                            com.vylo.signup.R.id.signup_nav_graph,
                                            bundleOf(
                                                USER_DATA to
                                                        SocialUserData(
                                                            name,
                                                            imageUrl
                                                        ),
                                                TOKEN_TYPE to TokenType.FACEBOOK
                                            )
                                        )
                                    }
                                    activity.hideProgress()
                                }
                            }
                        }
                        val parameters = Bundle()
                        parameters.putString(FIELDS, PROFILE_DATA)
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        showMessage(resources.getString(R.string.label_cancel))
                    }

                    override fun onError(error: FacebookException) {
                        showMessage("${error.message}")
                    }
                })

            viewModel.responseError.observe(viewLifecycleOwner) {
                hideProgress()
                showMessage(it)
            }

            viewModel.responseSuccessLogIn.observe(viewLifecycleOwner) {
                hideProgress()
                setScreenType(ScreenType.MAIN)
                showMessage(it)
                throwStartScreen()
                activity.finish()
            }
        }
    }

    private fun onEmailClick() {
        navigateTo(com.vylo.signup.R.id.signup_nav_graph)
    }

    private fun onFacebookClick() {
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            callbackManager,
            listOf(PUBLIC_PROFILE)
        )
    }

    private fun onGoogleClick() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        googleAuthActivityResultLauncher.launch(signInIntent)
    }

    private fun onLogInClick() {
        Navigation.findNavController(viewBinder.root).navigate(R.id.toLoginFragment)
    }

    override fun onStop() {
        requireActivity().makeDefaultTheme()
        super.onStop()
    }
}

