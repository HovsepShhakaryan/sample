package com.vylo.main.responsemain.edit.presentation.fragment

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.video.editor.interfaces.OnCommandVideoListener
import com.video.editor.interfaces.OnPreviewListner
import com.video.editor.interfaces.OnVideoListener
import com.video.editor.utils.RealPathUtil
import com.video.editor.view.MediaType
import com.vylo.common.BaseFragment
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toVisible
import com.vylo.common.util.CATEGORY_NAME
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.enums.UploadFileType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.categorymain.category.domain.entity.CategoryItem
import com.vylo.main.component.sharedviewmodel.NavigationSharedViewModel
import com.vylo.main.databinding.FragmentCaptureBinding
import com.vylo.main.databinding.FragmentEditBinding
import com.vylo.main.responsemain.upload.common.RespondData
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.net.URI
import java.util.*
import kotlin.concurrent.schedule

class EditFragment(private val pager: ViewPager2) : BaseFragment<FragmentEditBinding>(), OnVideoListener,
    OnCommandVideoListener, OnPreviewListner {
    override fun getViewBinding() = FragmentEditBinding.inflate(layoutInflater)
    private val sharedViewModel by sharedViewModel<NavigationSharedViewModel>()
    private var uriForUpload: Uri? = null
    private lateinit var activity: MainFlawActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinder = FragmentEditBinding.inflate(inflater, container, false)
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainFlawActivity
    }

    fun beginning() {
        sharedViewModel.responseUriUploadFile.observe(viewLifecycleOwner) {
            if (it != null) {
                uriForUpload = it

                viewBinder.videoTrimmer
                    .setMediaType(if (sharedViewModel.responseType == 1) MediaType.AUDIO else MediaType.VIDEO)
                    .setOnCommandListener(this)
                    .setOnVideoListener(this)
                    .setPreviewListner(this)
                    .setVideoURI(uriForUpload!!)
                    .setVideoInformationVisibility(true)
                    .setMinDuration(1)
                    .setDestinationPath(
                        Environment.getExternalStorageDirectory()
                            .toString() + File.separator + "${if (sharedViewModel.responseType == 1) "Music" else "Movies"}/Vylo" + File.separator
                    )
            }
        }


        createNavigationBar()
    }

    private fun createNavigationBar() {
        viewBinder.navBar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            val typeface =
                ResourcesCompat.getFont(requireContext(), com.vylo.common.R.font.suisse_lntl_medium)
            setTitleFontFamily(typeface)

            setTitle(resources.getString(R.string.label_edit))
            setNextBorderButtonText(resources.getString(R.string.label_next))
            setEnabledOnButtonBorderNext(false)

            setTitleStyle(com.vylo.common.R.style.MainText_H4)
            setNextBorderButtonStyle(com.vylo.common.R.drawable.shape_transparent_rect)
            setNextBorderButtonTextStyle(com.vylo.common.R.style.MainText_H8_link)
            clickOnButtonBack { backToPrevious() }
            clickOnButtonBorderNext {
                activity.showProgress()
                viewBinder.videoTrimmer.save()
            }
        }
    }

    private fun preparePublish(uri: Uri) {
        sharedViewModel.setEditUri(uri)
        sharedViewModel.setThumbnailUri(uri)
        pager.currentItem = if (sharedViewModel.responseType == 2) 1 else 2
    }

    override fun onVideoPrepared() {

    }

    override fun onStarted() {

    }

    override fun getResult(uri: Uri) {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, uri)

        val duration =mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
        val width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toLong()
        val height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toLong()
        val values = ContentValues()
        values.put(MediaStore.Video.Media.DATA, uri.path)
        values.put(MediaStore.Video.VideoColumns.DURATION, duration)
        values.put(MediaStore.Video.VideoColumns.WIDTH, width)
        values.put(MediaStore.Video.VideoColumns.HEIGHT, height)

        val id = ContentUris.parseId(
            context?.contentResolver?.insert(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values
            )!!
        )
        Log.e("VIDEO ID", id.toString())

        preparePublish(uri)
        activity.hideProgress()
    }

    override fun cancelAction() {

    }

    override fun onError(message: String) {
        println(message)
        showMessage(message)
    }

    override fun onPreviewLoaded() {
        activity.hideProgress()
        viewBinder.navBar.setEnabledOnButtonBorderNext(true)
    }
}