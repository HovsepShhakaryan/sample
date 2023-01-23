package com.vylo.main.profilefragment.presentation.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.exifinterface.media.ExifInterface
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.vylo.common.BaseFragment
import com.vylo.common.entity.RespondData
import com.vylo.common.ext.hideKeyboard
import com.vylo.common.util.PARENT_FRAGMENT
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.RESPONSE_TYPE
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.component.entity.ProfileInfo
import com.vylo.main.component.entity.mappers.toProfileInfo
import com.vylo.main.databinding.FragmentEditProfileBinding
import com.vylo.main.profilefragment.domain.entity.request.ProfileRequest
import com.vylo.main.profilefragment.presentation.viewmodel.EditProfileViewModel
import com.vylo.main.settingsprivacy.presentation.viewmodel.SettingsSharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    private val viewModel by viewModel<EditProfileViewModel>()
    private val settingsViewModel by sharedViewModel<SettingsSharedViewModel>()
    override fun getViewBinding() = FragmentEditProfileBinding.inflate(layoutInflater)

    private lateinit var activity: MainFlawActivity

    private var newPhoto: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onResume() {
        super.onResume()
        settingsViewModel.getProfileInfo()?.let {
            setViewData(it)
        }
        activity.showNavBar()
    }

    private var photoFile: File? = null

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
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let { uri ->
                    val bitmap = compressBitmap(rotateImage(uri))
                    photoFile = saveToTempFile(bitmap)
                    newPhoto = bitmap
                }
            }
        }

    private fun beginning() {
        createToolbar()
        createContentOfView()
        requirePermissions()
    }

    private fun createToolbar() {
        activity = getActivity() as MainFlawActivity
        viewBinder.toolbar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            clickOnButtonBack { backToPrevious() }
            setTitle(resources.getString(R.string.label_edit_profile))
            setStyleButtonBackText(com.vylo.common.R.style.MainText_H6_3)
            setNextBorderButtonStyle(com.vylo.common.R.drawable.shape_white_rect)
            setNextBorderButtonTextStyle(com.vylo.common.R.style.MainText_H8_4)
            setNextBorderButtonText(resources.getString(R.string.button_profile_save))
            clickOnButtonBorderNext { onSaveClick() }
        }
    }

    private fun createContentOfView() {
        viewModel.responseSuccess.observe(viewLifecycleOwner) {
            settingsViewModel.setProfileInfo(it.toProfileInfo())
            showMessage(resources.getString(R.string.data_save_successfully))
            backToPrevious()
        }

        viewModel.responseError.observe(viewLifecycleOwner) {
            showMessage(it)
        }

        viewModel.responsePhotoSuccess.observe(viewLifecycleOwner) { photoItem ->
            settingsViewModel.getProfileInfo()?.let {
                settingsViewModel.setProfileInfo(it.copy(profilePhoto = photoItem.profilePhoto))
            }
        }
    }

    private fun setViewData(info: ProfileInfo) {
        viewBinder.apply {
            if (newPhoto == null) {
                Picasso.get()
                    .load(info.profilePhoto)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.portrait_placeholder)
                    .into(image)
            } else {
                image.setImageBitmap(newPhoto)
            }

            name.setDesc(info.name.orEmpty())
            username.setDesc(info.username.orEmpty())
            website.setDesc(info.website.orEmpty())
            bio.setDesc(info.bio.orEmpty())
            bio.disableLineBreak()

            labelChangePhoto.setOnClickListener {
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

            labelPersonalInformationSettings.setOnClickListener {
                requireActivity().hideKeyboard()
                onPersonalInformationClick()
            }
        }
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

    private fun onSaveClick() {
        requireActivity().hideKeyboard()
        viewBinder.apply {
            focusWrapper.requestFocus()

            val websiteName = if (
                website.getDesc().contains("http://") ||
                website.getDesc().contains("https://")) website.getDesc()
            else "http://${website.getDesc()}"

            ProfileRequest(
                username = username.getDesc(),
                name = name.getDesc(),
                website = if (website.getDesc().isNotEmpty()) websiteName else "",
                bio = bio.getDesc()
            ).let {
                photoFile?.let { it1 ->
                    viewModel.updateProfilePhoto(it1)
                }
                viewModel.updateProfile(it)
            }
        }
    }

    private fun onPersonalInformationClick() {
        navigateTo(
            R.id.toPersonalInformationFragment,
            bundleOf(
                PARENT_FRAGMENT to simpleName
            )
        )
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

    companion object {
        val simpleName: String
            get() = EditProfileFragment().javaClass.simpleName

        private var PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.CAMERA
        )

        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
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