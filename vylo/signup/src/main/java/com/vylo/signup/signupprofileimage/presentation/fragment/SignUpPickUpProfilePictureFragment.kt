package com.vylo.signup.signupprofileimage.presentation.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.squareup.picasso.Picasso
import com.vylo.common.BaseFragment
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.ScreenType
import com.vylo.signup.R
import com.vylo.signup.databinding.FragmentSignUpPickUpProfilePictureBinding
import com.vylo.signup.signupglobal.viewmodel.SignUpSharedViewModel
import com.vylo.signup.signupprofileimage.presentation.viewmodel.SignUpPickUpProfilePictureFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class SignUpPickUpProfilePictureFragment :
    BaseFragment<FragmentSignUpPickUpProfilePictureBinding>() {

    override fun getViewBinding() =
        FragmentSignUpPickUpProfilePictureBinding.inflate(layoutInflater)

    private val viewModel by viewModel<SignUpPickUpProfilePictureFragmentViewModel>()
    private val sharedViewModel by sharedViewModel<SignUpSharedViewModel>()
    private var file: File? = null
    private var newPhoto: Bitmap? = null


    companion object {

        private var PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.CAMERA
        )

        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PERMISSIONS_REQUIRED && !it.value)
                    permissionGranted = false
            }
        }

    private val getPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.data?.let { uri ->
                    val bitmap = compressBitmap(rotateImage(uri))
                    file = saveToTempFile(bitmap)
                    newPhoto = bitmap

                    viewBinder.buttonAddImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity(),
                            R.drawable.ic_edit_prof_pic
                        )
                    )

                    viewBinder.nextButton.roundedWhiteButtonStyle(
                        requireContext(),
                        resources.getString(R.string.label_next),
                        ButtonStyle.ROUNDED_BIG_MEDIUM
                    )
                    viewBinder.nextButton.setEnableOrDisabel(true)
                }
            }
        }

    private fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column)
        } finally {
            cursor?.close()
        }
    }

    private fun rotateImage(uri: Uri): Bitmap {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }

        var rotate = 0
        return getRealPathFromURI(requireContext(), uri)?.let { path ->
            val exif = ExifInterface(path)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
            val matrix = Matrix()
            matrix.postRotate(rotate.toFloat())

            return Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width,
                bitmap.height, matrix, true
            )
        } ?: run {
            bitmap
        }
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        val divider = bitmap.width / 120

        return Bitmap.createScaledBitmap(
            bitmap,
            bitmap.width / divider,
            bitmap.height / divider,
            false
        )
    }

    private fun saveToTempFile(bitmap: Bitmap): File {
        val path = requireContext().externalCacheDir?.absolutePath
        val appRootFolder = File(path, "vylo")

        if (!appRootFolder.exists()) {
            appRootFolder.mkdir().let {
                if (!it) {
                    Log.d("EditProfileFragment", "dir not created")
                }
            }
        }

        val file = File(appRootFolder, "temp.jpeg")
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
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

    override fun onResume() {
        super.onResume()
        setPhotoImage()
    }

    private fun beginning() {
        createToolBar()
        createContentOfView()
        requirePermissions()
    }

    private fun createToolBar() {
        viewBinder.toolbar.setTitleImage(com.vylo.common.R.drawable.ic_vylo_name_new)
    }

    private fun createContentOfView() {
        viewBinder.nextButton.apply {
            viewBinder.nextButton.setEnableOrDisabel(false)
            roundedGrayButtonStyle(requireContext(), resources.getString(R.string.label_next))
            clickOnButton { onNextClick() }
        }

        viewBinder.buttonAddImage.setOnClickListener {
            if (hasPermissions(requireContext())) {
                Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_PICK
                    data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }.let {
                    getPhoto.launch(it)
                }
            } else {
                requirePermissions()
            }
        }

        viewBinder.buttonSkip.setOnClickListener {
//            throwUserIntoTheApp()
            onNextClick()
        }

        viewModel.responsePhotoError.observe(viewLifecycleOwner) {
            hideProgress()
            showMessage(it)
        }

        viewModel.responsePhotoSuccess.observe(viewLifecycleOwner) {
//            clearSharedViewModelData()
            hideProgress()
//            throwUserIntoTheApp()
            navigateTo(R.id.action_signUpPickUpProfilePictureFragment_to_signUpDoNotMissOutFragment)
//            navigateTo(R.id.action_signUpPickUpProfilePictureFragment_to_signUpChooseCategoryFragment)
        }
    }

    private fun setPhotoImage() {
        sharedViewModel.getSocialUserData()?.userPhoto?.let { profileImage ->
            Picasso.get().load(profileImage)
                .placeholder(com.vylo.common.R.drawable.portrait_placeholder)
                .into(viewBinder.profileImage)
            saveBitmap()
            viewBinder.nextButton.roundedWhiteButtonStyle(
                requireContext(),
                resources.getString(R.string.label_next),
                ButtonStyle.ROUNDED_BIG_MEDIUM
            )
            viewBinder.nextButton.setEnableOrDisabel(true)
        } ?: run {
            newPhoto?.let {
                viewBinder.profileImage.setImageBitmap(it)
                saveBitmap()
            }
        }
    }

    private fun saveBitmap() {
        viewBinder.profileImage.drawable?.let {
            val bitmap = (it as BitmapDrawable).bitmap
            file = saveToTempFile(bitmap)
        }
    }

    private fun throwUserIntoTheApp() {
        clearSharedViewModelData()
        setScreenType(ScreenType.MAIN)
        throwStartScreen()
        activity?.finish()
    }

    private fun clearSharedViewModelData() {
        sharedViewModel.setSignUp(null)
        sharedViewModel.setSocialUserData(null)
    }

    private fun requirePermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionList = PERMISSIONS_REQUIRED.toMutableList()
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PERMISSIONS_REQUIRED = permissionList.toTypedArray()
        }

        if (!hasPermissions(requireContext()))
            activityResultLauncher.launch(PERMISSIONS_REQUIRED)
    }

    private fun onNextClick() {
        if (file != null) {
            showProgress()
            viewModel.updateProfilePhoto(file!!)
        } else {
//            throwUserIntoTheApp()
            navigateTo(R.id.action_signUpPickUpProfilePictureFragment_to_signUpDoNotMissOutFragment)
//            navigateTo(R.id.action_signUpPickUpProfilePictureFragment_to_signUpChooseCategoryFragment)
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