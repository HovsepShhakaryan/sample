package com.vylo.signup.signupdonotmissout.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vylo.common.BaseFragment
import com.vylo.common.adapter.decorator.MarginItemDecoration
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ScreenType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpDoNotMissOutBinding
import com.vylo.signup.signupdonotmissout.presentation.adapter.DoNotMissOutAdapter
import com.vylo.signup.signupdonotmissout.presentation.viewmodel.SignUpDoNotMissOutFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpDoNotMissOutFragment : BaseFragment<FragmentSignUpDoNotMissOutBinding>() {

    override fun getViewBinding() = FragmentSignUpDoNotMissOutBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SignUpDoNotMissOutFragmentViewModel>()

    private val adapter: DoNotMissOutAdapter by lazy {
        DoNotMissOutAdapter(
            onFollowClick = ::onFollowClick
        )
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
        val buttonBack = View.OnClickListener {
            navigateTo(R.id.action_signUpDoNotMissOutFragment_to_signUpPickUpProfilePictureFragment)
        }
        viewBinder.toolbar.clickOnButtonBack(buttonBack)
    }

    private fun createContentOfView() {
        viewModel.getAutoFollow()

        viewModel.autoFollowError.observe(viewLifecycleOwner) {
            showMessage(it)
        }

        viewModel.autoFollowSuccess.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.publishersSuccess.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                setButtonStyle(false)
            } else {
                setButtonStyle(true)
            }
        }

        viewBinder.nextButton.apply {
            roundedGrayButtonStyle(requireContext(), resources.getString(R.string.label_next))
            clickOnButton { onNextClick() }
            setButtonStyle(true)
            setEnableOrDisabel(true)
        }

        viewBinder.apply {
            list.adapter = adapter
            list.addItemDecoration(
                MarginItemDecoration(
                    bottom = resources.getDimensionPixelSize(
                        com.vylo.common.R.dimen.margin_padding_size_large_small
                    )
                )
            )
        }
    }

    private fun setButtonStyle(isEnable: Boolean) {
        viewBinder.apply {
            nextButton.setEnableOrDisabel(isEnable)
            if (isEnable) {
                viewBinder.nextButton.roundedWhiteButtonStyle(
                    requireContext(),
                    resources.getString(R.string.label_next),
                    ButtonStyle.ROUNDED_BIG_MEDIUM
                )
                viewBinder.nextButton.setEnableOrDisabel(true)
            } else {
                viewBinder.nextButton.roundedGrayButtonStyle(
                    requireContext(),
                    resources.getString(R.string.label_next)
                )
                viewBinder.nextButton.setEnableOrDisabel(false)
            }
        }
    }

    private fun onFollowClick(id: String, isFollow: Boolean) {
        if (isFollow) {
            viewModel.follow(id)
        } else {
            viewModel.unfollow(id)
        }
    }

    private fun onNextClick() {
        navigateTo(R.id.action_signUpDoNotMissOutFragment_to_onboardingFragment)
    }

}