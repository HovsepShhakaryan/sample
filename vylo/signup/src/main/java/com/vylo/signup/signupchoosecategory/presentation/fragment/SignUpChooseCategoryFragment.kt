package com.vylo.signup.signupchoosecategory.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vylo.common.BaseFragment
import com.vylo.common.util.enums.ButtonType
import com.vylo.common.util.enums.ScreenType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpChooseCategoryBinding
import com.vylo.signup.signupchoosecategory.domain.entity.ChipList
import com.vylo.signup.signupchoosecategory.domain.entity.response.CategoryItem
import com.vylo.signup.signupchoosecategory.presentation.adapter.CategoryChipAdapter
import com.vylo.signup.signupchoosecategory.presentation.viewmodel.SignUpChooseCategoryFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpChooseCategoryFragment : BaseFragment<FragmentSignUpChooseCategoryBinding>(), CategoryChipAdapter.AdapterCallBack {

    override fun getViewBinding() = FragmentSignUpChooseCategoryBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SignUpChooseCategoryFragmentViewModel>()

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
        viewBinder.nextButton.apply {
            val paddingOfButton = resources.getDimension(R.dimen.padding_50).toInt()
            val styleId = com.vylo.common.R.style.MainText_H7
            setButtonShape(ButtonType.BUTTON_WITHOUT_BORDER_ROUND)
            val text = "${resources.getString(R.string.label_select_points)} 3 ${resources.getString(R.string.label_select_points_to_continue)}"
            setTitle(text)
            setStyle(styleId)
            setPaddingToButton(paddingOfButton, 0, paddingOfButton, 0)
            setButtonColor(ContextCompat.getColor(requireContext(), R.color.black_disable))
            clickOnButton { onNextClick() }
            setEnableOrDisabel(false)
        }

        viewBinder.buttonSkip.setOnClickListener {
            setScreenType(ScreenType.MAIN)
            throwStartScreen()
            activity?.finish()
        }

        viewModel.getCategory(null)

        viewModel.responseError.observe(viewLifecycleOwner) { showMessage(it) }

        viewModel.responseSuccess.observe(viewLifecycleOwner) {

            if (it != null) {
                var count = 0
                val chipRowOdd = mutableListOf<CategoryItem>()
                val chipRowMal = mutableListOf<CategoryItem>()

                for (chip in it) {
                    count++
                    if (count % 2 == 0) chipRowMal.add(chip)
                    else if (count % 2 == 1) chipRowOdd.add(chip)
                }

                var index = 0
                var chipOne: CategoryItem?
                var chipTwo: CategoryItem?
                val categoryRowList = mutableListOf<ChipList>()
                while (index != chipRowOdd.size) {
                    chipOne = chipRowOdd[index]
                    chipTwo = if (index < chipRowMal.size)
                        chipRowMal[index]
                    else null
                    index++
                    categoryRowList.add(ChipList(
                        chipOne,
                        chipTwo
                    ))
                }

                viewBinder.chipGroup.layoutManager = LinearLayoutManager(requireContext())
                val adapter = CategoryChipAdapter(this, requireContext())
                viewBinder.chipGroup.adapter = adapter
                adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                adapter.setData(categoryRowList)
            }
        }
    }

    private fun onNextClick() {
        navigateTo(R.id.action_signUpChooseCategoryFragment_to_signUpDoNotMissOutFragment)
    }

    override fun onChipClicked(id: Long, isChecked: Boolean) {
        when (isChecked) {
            false -> viewModel.deleteCategory(id)
            true -> viewModel.addCategory(id)
        }
    }

    override fun countCheckedChip(count: Int) {
        if (count > 2) {
            viewBinder.nextButton.setTitle(resources.getString(R.string.label_next))
            viewBinder.nextButton.setEnableOrDisabel(true)
            viewBinder.nextButton.setTitleColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.main
                )
            )
            viewBinder.nextButton.setButtonColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.secondary
                )
            )
        } else {
            val text = "${resources.getString(R.string.label_select_points)} ${3 - count} ${resources.getString(R.string.label_select_points_to_continue)}"
            viewBinder.nextButton.setTitle(text)
            viewBinder.nextButton.setEnableOrDisabel(false)
            viewBinder.nextButton.setTitleColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.white
                )
            )
            viewBinder.nextButton.setButtonColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black_disable
                )
            )
        }
    }

}