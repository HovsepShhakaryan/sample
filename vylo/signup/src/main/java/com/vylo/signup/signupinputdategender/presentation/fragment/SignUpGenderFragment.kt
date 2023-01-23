package com.vylo.signup.signupinputdategender.presentation.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.BaseFragment
import com.vylo.common.util.Converter
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ScreenType
import com.vylo.common.widget.MainInputType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpGenderBinding
import com.vylo.signup.signupglobal.entity.SignUp
import com.vylo.signup.signupglobal.viewmodel.SignUpSharedViewModel
import com.vylo.signup.signupinputdategender.domain.entity.GenderInner
import com.vylo.signup.signupinputdategender.presentation.adapter.GenderAdapter
import com.vylo.signup.signupinputdategender.presentation.viewmodel.SignUpGenderFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class SignUpGenderFragment : BaseFragment<FragmentSignUpGenderBinding>(), GenderAdapter.AdapterCallBack {

    override fun getViewBinding() = FragmentSignUpGenderBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SignUpGenderFragmentViewModel>()
    private val sharedViewModel by sharedViewModel<SignUpSharedViewModel>()
    private lateinit var adapter: GenderAdapter
    private lateinit var genderList: List<GenderInner>
    private var isFullNameFiled: Boolean = false
    private var isDateFiled: Boolean = false
    private var isGenderChosen: Boolean = false
    private var gender: String? = null
    private var isSignUpFromSocial = false

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
        this.view?.isFocusableInTouchMode = true
        this.view?.requestFocus()
        this.view?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                onBackPressed(isSignUpFromSocial)
                return@OnKeyListener true
            }
            false
        })
    }

    private fun onBackPressed(isSignUpFromSocial: Boolean) {
        if (isSignUpFromSocial) {
            setScreenType(ScreenType.AUTH)
            throwStartScreen()
            activity?.finish()
        } else requireActivity().onBackPressed()
    }

    private fun createToolBar() {
        viewBinder.toolbar.setTitleImage(com.vylo.common.R.drawable.ic_vylo_name_new)
        viewBinder.toolbar.setIconOfButtonBack(R.drawable.ic_circleback)
        val buttonBack = View.OnClickListener { onBackPressed(isSignUpFromSocial) }
        viewBinder.toolbar.clickOnButtonBack(buttonBack)
    }

    private fun createContentOfView() {
        viewBinder.inputName.apply {
            val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
            initRectInput(
                R.string.label_full_name,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            )
            val shapeType = com.vylo.common.util.enums.InputType.INPUT_ROUND
            setInputShape(shapeType)
            showHideLine(View.VISIBLE)
            addTextChangedListenerValidation(this, ::checkInputValidation, iconOfValidation)
            if (sharedViewModel.getSocialUserData() != null) {
                setInputText(sharedViewModel.getSocialUserData()!!.userName)
                isSignUpFromSocial = true
            }
        }

        viewBinder.inputDate.apply {
            initRectInput(
                R.string.label_birth_date,
                (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
            )
            val shapeType = com.vylo.common.util.enums.InputType.INPUT_ROUND
            setInputShape(shapeType)
            showHideLine(View.VISIBLE)
            disableInput()
            clickOnContainer { openDatePicker() }
        }


        viewBinder.nextButton.apply {
            roundedGrayButtonStyle(requireContext(), resources.getString(R.string.label_next))
            clickOnButton { onNextClick() }
            setEnableOrDisabel(false)
        }

        genderList = listOf(
            GenderInner(resources.getString(R.string.label_male), false),
            GenderInner(resources.getString(R.string.label_female), false),
            GenderInner(resources.getString(R.string.label_other), false)
        )

        adapter = GenderAdapter(this, requireContext())
        adapter.setData(genderList)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinder.listGender.layoutManager = linearLayoutManager
        viewBinder.listGender.adapter = adapter
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

    }

    private fun openDatePicker() {
        val mCalendar: Calendar = GregorianCalendar()
        mCalendar.time = Date()
        DatePickerDialog(requireContext(), com.vylo.common.R.style.my_dialog_theme, { view, year, monthOfYear, dayOfMonth ->
            val monthOfYearCounted = when (monthOfYear) {
                0 -> 1
                else -> monthOfYear + 1
            }
            val datePickerDateFormat = "$dayOfMonth/$monthOfYearCounted/$year"
            val dateOfBirthFormat = Converter.fromDatePickerToDate(datePickerDateFormat)
            viewBinder.inputDate.setInputText(dateOfBirthFormat)
            isDateFiled = true
            val iconOfValidation = com.vylo.common.R.drawable.ic_check_mark
            viewBinder.inputDate.setIcon(iconOfValidation)
            activateNextButton()
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun onNextClick() {
        val signUpModel = if (sharedViewModel.getSignUp() != null) {
            sharedViewModel.getSignUp()?.copy(
                name = viewBinder.inputName.getInputText(),
                gender = gender!!.lowercase(),
                birthday_date = Converter.fromDatePickerToDateForServer(viewBinder.inputDate.getInputText())
            )
        } else {
            SignUp(
                name = viewBinder.inputName.getInputText(),
                gender = gender!!.lowercase(),
                birthday_date = Converter.fromDatePickerToDateForServer(viewBinder.inputDate.getInputText())
            )
        }

        sharedViewModel.setSignUp(signUpModel)
        if (sharedViewModel.getSocialUserData() != null) navigateTo(R.id.action_signUpGenderFragment_to_signUpCreateUsernameFragment)
        else navigateTo(R.id.action_signUpGenderFragment_to_createPasswordFragment)
    }

    private fun activateNextButton() {
        if (isDateFiled && isFullNameFiled && isGenderChosen) {
            viewBinder.nextButton.roundedWhiteButtonStyle(
                requireContext(),
                resources.getString(R.string.label_next),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            viewBinder.nextButton.setEnableOrDisabel(true)
        } else {
            viewBinder.nextButton.roundedGrayButtonStyle(requireContext(), resources.getString(R.string.label_next))
            viewBinder.nextButton.setEnableOrDisabel(false)
        }
    }

    private fun checkInputValidation(view: MainInputType) =
        when (viewModel.fullNameIsNotEmpty(view.getInputText())) {
            "" -> {
                isFullNameFiled = true
                activateNextButton()
                true
            }
            else -> {
                isFullNameFiled = false
                activateNextButton()
                false
            }
        }


    override fun itemClicked(genderItem: GenderInner) {
        gender = genderItem.genderName
        val newGenderList = mutableListOf<GenderInner>()
        genderList.forEach {
            if (it == genderItem) {
                newGenderList.add(
                    GenderInner(
                        genderName = it.genderName,
                        isChosen = true
                    )
                )
            } else newGenderList.add(it)
        }

        adapter.clearData()
        adapter.setData(newGenderList)

        isGenderChosen = adapter.getData().any { it.isChosen == true }
        activateNextButton()
    }

}