package com.vylo.signup.signuppickprofilepicture.presentation.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.vylo.common.BaseFragment
import com.vylo.common.util.enums.ButtonType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpPickUpProfilePictureBinding

class SignUpPickUpProfilePictureFragment : BaseFragment<FragmentSignUpPickUpProfilePictureBinding>() {

    override fun getViewBinding() = FragmentSignUpPickUpProfilePictureBinding.inflate(layoutInflater)

    private val pickPictureFromGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        if (result?.resultCode == RESULT_OK) {
            val data: Intent = result.data!!

            val bitmap = when {
                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    data.data
                )
                else -> {
                    val source = ImageDecoder.createSource(requireActivity().contentResolver, data.data!!)
                    ImageDecoder.decodeBitmap(source)
                }
            }

            viewBinder.profileImage.setImageBitmap(bitmap)
            viewBinder.buttonAddImage.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_edit_prof_pic))
        }
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
        createToolBar()
        createContentOfView()
    }

    private fun createToolBar() {
        viewBinder.toolbar.setTitleImage(com.vylo.common.R.drawable.ic_vylo_name_new)
    }

    private fun createContentOfView() {
        viewBinder.nextButton.apply {
            val paddingOfButton = resources.getDimension(R.dimen.padding_50).toInt()
            val styleId = com.vylo.common.R.style.MainText_H7_1_Black
            setButtonShape(ButtonType.BUTTON_WITHOUT_BORDER_ROUND)
            setTitle(resources.getString(R.string.label_next))
            setStyle(styleId)
            setPaddingToButton(paddingOfButton, 0, paddingOfButton, 0)
            setButtonColor(ContextCompat.getColor(requireContext(), R.color.secondary))
            clickOnButton { onNextClick() }
        }

        viewBinder.buttonAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickPictureFromGallery.launch(intent)
        }

        viewBinder.buttonSkip.setOnClickListener {

        }
    }

    private fun onNextClick() {
        navigateTo(R.id.action_signUpPickUpProfilePictureFragment_to_signUpChooseCategoryFragment)
    }
}