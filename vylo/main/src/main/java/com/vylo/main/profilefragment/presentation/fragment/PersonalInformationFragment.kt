package com.vylo.main.profilefragment.presentation.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.vylo.common.BaseFragment
import com.vylo.common.entity.RespondData
import com.vylo.common.ext.hideKeyboard
import com.vylo.common.util.Converter
import com.vylo.common.util.PARENT_FRAGMENT
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.main.R
import com.vylo.main.component.entity.ProfileInfo
import com.vylo.main.component.entity.mappers.toProfileInfo
import com.vylo.main.databinding.FragmentPersonalInformationBinding
import com.vylo.main.profilefragment.domain.entity.request.ProfileRequest
import com.vylo.main.profilefragment.presentation.viewmodel.EditProfileViewModel
import com.vylo.main.settingsprivacy.presentation.viewmodel.SettingsSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class PersonalInformationFragment : BaseFragment<FragmentPersonalInformationBinding>() {

    private val viewModel by viewModel<EditProfileViewModel>()
    private val settingsViewModel by sharedViewModel<SettingsSharedViewModel>()

    override fun getViewBinding() = FragmentPersonalInformationBinding.inflate(layoutInflater)

    private var year = 2000
    private var month = 0
    private var day = 1

    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        this.year = year
        this.month = month
        this.day = day

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        viewBinder.birthday.setDesc(Converter.fromLongToFrontEndFormat(calendar.time.time))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    fun beginning() {
        createToolbar()
        createContentOfView()
    }

    private fun createToolbar() {
        viewBinder.toolbar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack {
                requireActivity().hideKeyboard()
                backToPrevious()
            }
            setTitle(resources.getString(R.string.label_personal_information))
            setStyleButtonBackText(com.vylo.common.R.style.MainText_H6_3)

            setNextBorderButtonStyle(com.vylo.common.R.drawable.shape_white_without_border)
            setNextBorderButtonTextStyle(com.vylo.common.R.style.White_square_rounded_button_medium_small)
            setNextBorderButtonText(resources.getString(R.string.button_profile_save))

            clickOnButtonBorderNext { onSaveClick() }
            showBottomBorder(View.VISIBLE)
            setColorBottomBorder(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.dark_grey
                )
            )
            setHeightBottomBorder(R.dimen.half_one)
        }
    }

    private fun createContentOfView() {
        settingsViewModel.getProfileInfo()?.let { info ->
            setViewData(info)
        }

        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            settingsViewModel.setProfileInfo(it.toProfileInfo())
            showMessage(resources.getString(R.string.data_save_successfully))
        }

        viewModel.responseError.observe(viewLifecycleOwner) {
            showMessage(it)
        }
    }

    private fun setViewData(info: ProfileInfo) {
        viewBinder.apply {
            email.setDesc(info.email.orEmpty())
            phone.setDesc(info.phone.orEmpty())
            gender.setDesc(info.gender.orEmpty())

            val birthDate = if (info.birthdayDate.isNullOrEmpty()) {
                ""
            } else {
                year = Converter.fromBackEndToYearFormat(info.birthdayDate.orEmpty())
                month = Converter.fromBackEndToMonthFormat(info.birthdayDate.orEmpty())
                day = Converter.fromBackEndToDayFormat(info.birthdayDate.orEmpty())
                Converter.fromBackEndToFrontEndFormat(info.birthdayDate.orEmpty())
            }
            birthday.setDesc(birthDate)

            gender.onDescClickListener {
                requireActivity().hideKeyboard()
                focusWrapper.requestFocus()

                GenderDialogFragment {
                    gender.setDesc(it)
                }.show(requireActivity().supportFragmentManager, null)
            }

            birthday.onDescClickListener {
                requireActivity().hideKeyboard()
                focusWrapper.requestFocus()

                onDateClick()
            }
        }
    }

    private fun getPreviousFragment() = arguments?.getString(PARENT_FRAGMENT)

    private fun onSaveClick() {
        requireActivity().hideKeyboard()
        viewBinder.apply {
            focusWrapper.requestFocus()

            ProfileRequest(
                phone = phone.getDesc(),
                gender = gender.getDesc(),
                birthdayDate = Converter.fromFrontEndToBackEndFormat(birthday.getDesc())
            ).let {
                viewModel.updatePersonalInformation(it)
            }
        }
    }

    private fun onDateClick() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            com.vylo.common.R.style.my_dialog_theme, dateSetListener, year, month, day
        )
        datePickerDialog.show()
    }

    override fun openCreateResponseScreen(
        id: String?,
        responseType: Int,
        title: String?,
        categoryId: String?,
        categoryName: String?
    ) {
        super.openCreateResponseScreen(id, responseType, title, categoryId, categoryName)
        if (!id.isNullOrEmpty())
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPOND_INFO to RespondData(
                        responseToGlobalId = id,
                        title!!,
                        categoryId!!,
                        categoryName!!
                    ),
                    RESPONSE_TYPE to responseType
                )
            )
        else
            navigateTo(
                R.id.navigationFragment,
                bundleOf(
                    RESPONSE_TYPE to responseType
                )
            )
    }
}