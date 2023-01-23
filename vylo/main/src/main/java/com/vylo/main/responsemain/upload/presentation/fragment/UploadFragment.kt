package com.vylo.main.responsemain.upload.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.video.editor.utils.BackgroundExecutor
import com.vylo.common.BaseFragment
import com.vylo.common.dialog.SimpleDialogFragment
import com.vylo.common.entity.ResponseUploadData
import com.vylo.common.entity.UploadData
import com.vylo.common.ext.toGone
import com.vylo.common.ext.toVisible
import com.vylo.common.util.CATEGORY_NAME
import com.vylo.common.util.RESPOND_INFO
import com.vylo.common.util.enums.ButtonStyle
import com.vylo.common.util.enums.UpdateStatus
import com.vylo.common.util.enums.UploadFileType
import com.vylo.main.R
import com.vylo.main.activity.presentation.activity.MainFlawActivity
import com.vylo.main.categorymain.category.domain.entity.CategoryItem
import com.vylo.main.component.sharedviewmodel.NavigationSharedViewModel
import com.vylo.main.databinding.FragmentUploadBinding
import com.vylo.main.responsemain.createvideo.domain.entity.response.ParametersOfUpload
import com.vylo.main.responsemain.upload.common.RespondData
import com.vylo.main.responsemain.upload.domain.entity.request.Thumbnail
import com.vylo.main.responsemain.upload.presentation.viewmodel.UploadFragmentViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.concurrent.schedule


class UploadFragment(
    private val pager: ViewPager2,
    private val isDestinationProfile: Boolean,
    private val uploadData: UploadData? = null
) : BaseFragment<FragmentUploadBinding>() {

    override fun getViewBinding() = FragmentUploadBinding.inflate(layoutInflater)
    private val sharedViewModel by sharedViewModel<NavigationSharedViewModel>()
    private val viewModel by sharedViewModel<UploadFragmentViewModel>()
    private var uriForUpload: Uri? = null
    private var audioBitmap: Bitmap? = null
    private lateinit var activity: MainFlawActivity
    private lateinit var categoryItem: CategoryItem
    private var parametersOfFileUpload: ParametersOfUpload? = null
    private var responseParameterGlobalId: String? = null
    private var responseData: ResponseUploadData? = null

    private var isStartedUpload = false
    private var isUploading = false

    private var thumbnailType: ThumbnailType = ThumbnailType.BITMAP
    private var thumbnailUri: Uri? = null
    private var thumbnailPresignedUrl: ParametersOfUpload? = null

    private val requestBody: RequestBody by lazy {
        val file = getRealPathFromURI(uriForUpload!!)?.let { it1 -> File(it1) }

        file!!.asRequestBodyWithProgress("".toMediaTypeOrNull()) { progress ->
            if(progress == 1f) {
                requireActivity().runOnUiThread(Runnable {
                    viewBinder.progressUpload.progressTintList = ColorStateList.valueOf(Color.GREEN)
                    isUploading = false
                    showUploading(true)
                })

                sendData()
            }
            requireActivity().runOnUiThread(Runnable {
                viewBinder.progressUpload.progress = (progress * 100).toInt()
            })
        }
    }

    private val thumbnailRequestBody: RequestBody by lazy {
        val file = getRealPathFromURI(thumbnailUri!!)?.let { it1 -> File(it1) }

        file!!.asRequestBodyWithProgress("".toMediaTypeOrNull()) { progress ->
            if(progress == 1f) {
                requireActivity().runOnUiThread(Runnable {
                    isUploading = true
                    viewModel.uploadVideo(requestBody, parametersOfFileUpload!!)
                })
            }
        }

    }

    private fun showUploading(completed: Boolean = false) {
        viewBinder.progressUpload.isVisible = isUploading
        viewBinder.lblUploading.isVisible = isUploading
        viewBinder.lblUploading.text = if (completed) "Complete." else "Uploading..."

        viewBinder.btnPublish.setEnableOrDisabel(completed)
    }

    fun File.asRequestBodyWithProgress(
        contentType: MediaType? = null,
        progressCallback: ((progress: Float) -> Unit)?
    ): RequestBody {
        return object : RequestBody() {
            override fun contentType() = contentType

            override fun contentLength() = length()

            override fun writeTo(sink: BufferedSink) {
                val fileLength = contentLength()
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                val inSt = FileInputStream(this@asRequestBodyWithProgress)
                var uploaded = 0L
                inSt.use {
                    var read: Int = inSt.read(buffer)
                    while (read != -1) {
                        progressCallback?.let {
                            uploaded += read
                            val progress = (uploaded.toDouble() / fileLength.toDouble()).toFloat()
                            it(progress)

                            sink.write(buffer, 0, read)
                        }
                        read = inSt.read(buffer)
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainFlawActivity
    }

    private fun setUploadData() {
        uploadData?.let {
            if (it.responseToGlobalId != null) {
                responseParameterGlobalId = it.responseToGlobalId
                responseData = ResponseUploadData(
                    it.responseToTitle.orEmpty(),
                    it.responseToCategoryId.orEmpty(),
                    it.responseToCategoryName.orEmpty()
                )
                return
            }
        }
        arguments?.getParcelable<com.vylo.common.entity.RespondData>(RESPOND_INFO)?.let {
            responseParameterGlobalId = it.responseToGlobalId
            responseData = ResponseUploadData(
                it.responseToTitle,
                it.responseCategoryId,
                it.responseCategoryName
            )
            return
        }
        sharedViewModel.responseVidoeParameters?.let {
            if (it.responseToTitle != null) {
                responseParameterGlobalId = it.responseToGlobalId
                responseData = ResponseUploadData(
                    it.responseToTitle.orEmpty(),
                    it.responseCategoryId.orEmpty(),
                    it.responseCategoryName.orEmpty()
                )
                return
            }
        }
        sharedViewModel.responseAudioParameters?.let {
            if (it.responseToTitle != null) {
                responseParameterGlobalId = it.responseToGlobalId
                responseData = ResponseUploadData(
                    it.responseToTitle.orEmpty(),
                    it.responseCategoryId.orEmpty(),
                    it.responseCategoryName.orEmpty()
                )
                return
            }
        }
        sharedViewModel.respondInfo?.let {
            responseParameterGlobalId = it.responseToGlobalId
            responseData = ResponseUploadData(
                it.responseToTitle,
                it.responseCategoryId,
                it.responseCategoryName
            )
            return
        }
    }

    private fun saveVideo() {
        if (uriForUpload == null) {
            SimpleDialogFragment(
                title = resources.getString(
                    R.string.label_error
                ),
                message = resources.getString(
                    R.string.label_upload_video
                ),
                okButtonTitle = resources.getString(R.string.label_ok),
                onOkClick = { }
            )
                .show(requireActivity().supportFragmentManager, null)
            return
        }
        if (arguments?.getParcelable<com.vylo.common.entity.RespondData>(RESPOND_INFO)?.responseToTitle != null
            || sharedViewModel.responseVidoeParameters?.responseToTitle != null
            || sharedViewModel.responseAudioParameters?.responseToTitle != null
            || uploadData?.responseToTitle != null
        ) {
            setUploadData()

            if (sharedViewModel.responseType == 1) {
                isUploading = true
                showUploading()
                viewModel.uploadAudio(requestBody, parametersOfFileUpload!!)
            } else {
                uploadThumbnail()
            }
            return
        }
        viewBinder.let {
            when {
                it.titleBody.text.toString().isEmpty() -> {
                    SimpleDialogFragment(
                        title = resources.getString(
                            R.string.label_error
                        ),
                        message = resources.getString(
                            R.string.label_input_title
                        ),
                        okButtonTitle = resources.getString(R.string.label_ok),
                        onOkClick = { }
                    )
                        .show(requireActivity().supportFragmentManager, null)
                }
                it.descBody.text.equals(resources.getString(R.string.label_description_body)) -> {
                    SimpleDialogFragment(
                        title = resources.getString(
                            R.string.label_error
                        ),
                        message = resources.getString(
                            R.string.label_select_category
                        ),
                        okButtonTitle = resources.getString(R.string.label_ok),
                        onOkClick = { }
                    )
                        .show(requireActivity().supportFragmentManager, null)
                }
                else -> {
                    if (sharedViewModel.responseType == 1) {
                        isUploading = true
                        showUploading()
                        viewModel.uploadAudio(requestBody, parametersOfFileUpload!!)
                    } else {
                        uploadThumbnail()
                    }
                }
            }
        }
    }

    private fun uploadThumbnail() {
        if (thumbnailType == ThumbnailType.BITMAP) {
            val filepath: String = requireContext().externalCacheDir!!.absolutePath
            val dir = File(filepath, "VyloThumbnail")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir,  "vylo_thumbnail.jpg")

            try {
                val fOut = FileOutputStream(file)
                viewBinder.thumbnail.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 85, fOut)
                fOut.flush()
                fOut.close()
                thumbnailUri = Uri.fromFile(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        isUploading = true
        showUploading()

        thumbnailPresignedUrl?.let { it1 -> viewModel.uploadThumbnail(it1, thumbnailRequestBody) }
    }


    private fun createNavigationBar() {
        viewBinder.navBar.apply {
            setIconOfButtonBack(com.vylo.common.R.drawable.ic_back_nav)
            val typeface =
                ResourcesCompat.getFont(requireContext(), com.vylo.common.R.font.suisse_lntl_medium)
            setTitleFontFamily(typeface)

            uploadData?.let {
                setTitle(resources.getString(R.string.label_add_details))
                setNextBorderButtonText(resources.getString(R.string.button_done))
            } ?: run {
                setTitle(resources.getString(R.string.label_add_details))
            }

            setTitleStyle(com.vylo.common.R.style.MainText_H4)
            setColorBottomBorder(
                ContextCompat.getColor(
                    requireContext(),
                    com.vylo.common.R.color.white_light_grey_text
                )
            )
            clickOnButtonBack { backToPrevious() }
        }
    }

    private fun createContentOfView() {
        viewBinder.btnPublish.roundedWhiteButtonStyle(
            requireContext(),
            resources.getString(R.string.label_publish),
            ButtonStyle.ROUNDED_BIG_MEDIUM
        )

        viewBinder.btnPublish.clickOnButton(View.OnClickListener { saveVideo() })
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? =
            requireContext().contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    override fun onResume() {
        super.onResume()
        if (uriForUpload != null) {
            if (!isStartedUpload && uploadData == null)
                getParametersData()
        } else sharedViewModel.setType(UploadFileType.UPLOAD)
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

    private fun getParametersData() {
        isStartedUpload = true

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uriForUpload)

        val durationInMilliseconds =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt()
        val durationInSeconds = durationInMilliseconds?.let { it / 1000 } ?: run { 0 }

        if (sharedViewModel.fileType == UploadFileType.VIDEO || sharedViewModel.fileType == UploadFileType.UPLOAD) {
            viewModel.getVideoPresignData(
                durationInSeconds,
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt(),
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
            )

            viewBinder.btnPublish.isEnabled = false
        } else if (sharedViewModel.fileType == UploadFileType.AUDIO) {
            viewModel.getAudioPresignData(durationInSeconds)
            viewBinder.btnPublish.isEnabled = false
        }
    }

    private fun getThumbnailPresignData() {
        val thumbnail = Thumbnail(cover_ext = "png")
        parametersOfFileUpload?.newsRecordGlobalId?.let {
            viewModel.getThumbnailPresignData(
                it,
                thumbnail
            )
        }
    }

    private fun showAudioImage(isShow: Boolean) {
        //viewBinder.thumbnail.visibility = if (isShow) View.VISIBLE else View.GONE
        if (isShow) {
            viewBinder.thumbnail.setImageBitmap(audioBitmap)
        }
    }

    private fun setBundleData(data: UploadData) {
        uriForUpload = Uri.parse(data.url)
        sharedViewModel.setType(UploadFileType.UPDATE)

        viewBinder.apply {
            titleBody.setText(data.title)
            descBody.text = data.responseToCategoryName
        }
    }

    private fun responseToVisible(title: String?) {
        title?.let {
            viewBinder.apply {
                titleHead.toGone()
                titleBody.toGone()
                descHead.toGone()
                descBody.toGone()

                responseToHead.toVisible()
                responseToBody.toVisible()
                responseToBody.text = it
            }
        }
    }

    private fun beginning() {
        viewBinder.btnPick.visibility = if (sharedViewModel.responseType == 1) View.GONE else View.VISIBLE
        sharedViewModel.responseVidoeParameters?.let {
            responseToVisible(it.responseToTitle)
        }
        sharedViewModel.responseAudioParameters?.let {
            responseToVisible(it.responseToTitle)
        }
        uploadData?.let {
            responseToVisible(it.responseToTitle)
        }
        sharedViewModel.respondInfo?.let {
            responseToVisible(it.responseToTitle)
        }
        arguments?.getParcelable<com.vylo.common.entity.RespondData>(RESPOND_INFO).let {
            it?.responseToTitle?.let { title ->
                viewBinder.apply {
                    titleHead.toGone()
                    titleBody.toGone()
                    descHead.toGone()
                    descBody.toGone()

                    responseToHead.toVisible()
                    responseToBody.toVisible()
                    responseToBody.text = title
                }
            }
        }
        uploadData?.let {
            categoryItem = CategoryItem(
                id = it.categoryId,
                categoryName = it.categoryName
            )
            setBundleData(it)
        }

        createNavigationBar()
        createContentOfView()

//        viewBinder.btnPublish.setOnClickListener {
//           saveVideo()
//        }

        viewBinder.btnPick.setOnClickListener {
            pager.currentItem = if (sharedViewModel.responseType == 2) 2 else 3
        }

        viewBinder.titleHead.setOnClickListener {
            showKeyboard(viewBinder.titleHead, requireContext())
            viewBinder.titleBody.requestFocus()
        }

        viewBinder.titleBody.setOnClickListener {
            showKeyboard(viewBinder.titleHead, requireContext())
        }

        viewBinder.descHead.setOnClickListener { pager.currentItem = if (sharedViewModel.responseType == 2) 3 else 4 }
        viewBinder.descBody.setOnClickListener { pager.currentItem = if (sharedViewModel.responseType == 2) 3 else 4 }

        sharedViewModel.responseUriEditVideoFile.observe(viewLifecycleOwner) {
            if (it != null) {
                uriForUpload = it
                if (sharedViewModel.responseType != 1) {
                    loadFrame(0)
                }
            }
        }

        sharedViewModel.responseAudioUriFile.observe(viewLifecycleOwner) {
            if (it != null)
                audioBitmap = BitmapFactory.decodeFile(it)
        }

        val mediaController = MediaController(requireContext())
        mediaController.visibility = View.GONE

        sharedViewModel.responseCategoryItem.observe(viewLifecycleOwner) {
            if (it != null) {
                categoryItem = it
                viewBinder.descBody.text = it.categoryName
            }

        }

        viewModel.responseUploadSuccess.observe(viewLifecycleOwner) {
            if (it != null) {
                activity.hideProgress()
                backToPrevious()
                if (!isDestinationProfile) {
                    activity.setData(bundleOf(CATEGORY_NAME to viewBinder.descBody.text.toString()))
                    activity.navigateTo(R.id.profile_main_graph)
                }
            }
        }

        viewModel.responseUploadError.observe(viewLifecycleOwner) {
            if (it != null) {
                activity.hideProgress()
                showMessage(it)
            }
        }

        viewModel.responseVideoParametersSuccess.observe(viewLifecycleOwner) { params ->
            if (params != null) {
                viewBinder.navBar.setEnabledOnButtonBorderNext(true)
                arguments?.getParcelable<RespondData>(RESPOND_INFO)?.let {
                    sharedViewModel.setParametersVideo(params.copy(responseToGlobalId = it.responseToGlobalId))
                } ?: run {
                    sharedViewModel.setParametersVideo(params)
                }

                showAudioImage(false)
                responseParameterGlobalId =
                    sharedViewModel.responseVidoeParameters?.responseToGlobalId
                parametersOfFileUpload = sharedViewModel.responseVidoeParameters

                getThumbnailPresignData()
            }
        }
        viewModel.responseAudioParametersSuccess.observe(viewLifecycleOwner) { params ->
            if (params != null) {
                viewBinder.btnPublish.isEnabled = true
                arguments?.getParcelable<RespondData>(RESPOND_INFO)?.let {
                    sharedViewModel.setParametersAudio(params.copy(responseToGlobalId = it.responseToGlobalId))
                } ?: run {
                    sharedViewModel.setParametersAudio(params)
                }

                showAudioImage(true)
                responseParameterGlobalId =
                    sharedViewModel.responseAudioParameters?.responseToGlobalId
                parametersOfFileUpload = sharedViewModel.responseAudioParameters

                //viewModel.uploadAudio(requestBody, parametersOfFileUpload!!)
            }
        }

        viewModel.responseThumbnailPresignedUrl.observe(viewLifecycleOwner) { param ->
            if (param != null) {
                viewBinder.btnPublish.isEnabled = true
                thumbnailPresignedUrl = param
            }
        }

        sharedViewModel.responseThumbnailBitmap.observe(viewLifecycleOwner) {
            if (it != null) {
                viewBinder.thumbnail.setImageBitmap(it)
                thumbnailType = ThumbnailType.BITMAP
            }
        }

        sharedViewModel.responseThumbnailGallery.observe(viewLifecycleOwner) {
            if (it != null) {
                println(it)
                thumbnailUri = it
                viewBinder.thumbnail.setImageURI(thumbnailUri)
                thumbnailType = ThumbnailType.GALLERY
            }
        }
    }

    private fun loadFrame(progress: Int) {
        BackgroundExecutor.execute(object : BackgroundExecutor.Task("", 0L, "") {
            override fun execute() {
                try {
                    val mediaMetadataRetriever = MediaMetadataRetriever()
                    mediaMetadataRetriever.setDataSource(context, uriForUpload)
                    val videoLengthInMs = (Integer.parseInt(
                        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ) * 1000).toLong()
                    val seekDuration = (videoLengthInMs * progress) / 1000
                    val bitmap = mediaMetadataRetriever.getFrameAtTime(
                        seekDuration * 10,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                    )
                    if (bitmap != null) {
                        try {
                            (context as Activity).runOnUiThread {
                                viewBinder.thumbnail.setImageBitmap(bitmap)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    mediaMetadataRetriever.release()
                } catch (e: Throwable) {
                    Thread.getDefaultUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e)
                }
            }
        })
    }

    private fun makeUpload(uri: Uri?) {
        if (uri != null) {
            if (parametersOfFileUpload != null) {
                parametersOfFileUpload?.newsRecordGlobalId?.let {
                    viewModel.updateColumns(it)
                }
            } else if (uploadData != null) {
                viewModel.updateColumns(uploadData.id)
            }
        }
    }

    private fun sendData() {
        responseData?.let {
            viewModel.setCategory(it.categoryId)
            viewModel.setTitle(it.title)
            viewModel.setDescription(it.categoryName)
        } ?: run {
            viewModel.setCategory(categoryItem.id)
            viewModel.setTitle(viewBinder.titleBody.text.toString())
            viewModel.setDescription(viewBinder.descBody.text.toString())
        }
        responseParameterGlobalId?.let {
            viewModel.setResponseTo(it)
        }
        viewModel.setStatus(UpdateStatus.PUBLISHED.status)
        makeUpload(uriForUpload)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearResponseTo()
    }
}