package com.vylo.main.component.sharedviewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.hovsep.camera.createvideo.domain.entity.GalleryEntityProperties
import com.vylo.common.SingleLiveEvent
import com.vylo.common.entity.RespondData
import com.vylo.common.util.enums.UploadFileType
import com.vylo.main.categorymain.category.domain.entity.CategoryItem
import com.vylo.main.responsemain.createvideo.domain.entity.response.ParametersOfUpload

class NavigationSharedViewModel : ViewModel() {

    val responseUriUploadFile: SingleLiveEvent<Uri> by lazy { SingleLiveEvent() }
    val responseUriEditVideoFile: SingleLiveEvent<Uri> by lazy { SingleLiveEvent() }
    val responseUriThumbail: SingleLiveEvent<Uri> by lazy { SingleLiveEvent() }
    val responseAudioUriFile: SingleLiveEvent<String> by lazy { SingleLiveEvent() }
    val responseCategoryItem: SingleLiveEvent<CategoryItem> by lazy { SingleLiveEvent() }
    val responseThumbnailBitmap: SingleLiveEvent<Bitmap> by lazy { SingleLiveEvent() }
    val responseThumbnailGallery: SingleLiveEvent<Uri> by lazy { SingleLiveEvent() }
    var responseVidoeParameters: ParametersOfUpload? = null
    var responseAudioParameters: ParametersOfUpload? = null
    var responseType: Int = 0
    lateinit var fileType: UploadFileType
    var properties: GalleryEntityProperties? = null
    var respondInfo: RespondData? = null

    private var frontDore: Boolean? = null

    fun setFrontDore(frontDore: Boolean) {
        this.frontDore = frontDore
    }
    fun getFrontDore() = frontDore

    fun setUri(uri: Uri?, fileType: UploadFileType) {
        setType(fileType)
        responseUriUploadFile.postValue(uri)
    }

    fun setEditUri(uri: Uri?) {
        responseUriEditVideoFile.postValue(uri)
    }

    fun setThumbnailUri(uri: Uri?) {
        responseUriThumbail.postValue(uri)
    }

    fun setType(fileType: UploadFileType) {
        this.fileType = fileType
    }

    fun setAudioUri(filePath: String?) {
        responseAudioUriFile.postValue(filePath)
    }

    fun setCategoryItem(categoryItem: CategoryItem) {
        responseCategoryItem.postValue(categoryItem)
    }

    fun setParametersVideo(parametersOfVideoUpload: ParametersOfUpload) {
        responseVidoeParameters = parametersOfVideoUpload
    }

    fun setParametersAudio(parametersOfVideoUpload: ParametersOfUpload) {
        responseAudioParameters = parametersOfVideoUpload
    }

    fun setThumbnailBitmap(bitmap: Bitmap) {
        responseThumbnailBitmap.postValue(bitmap)
    }

    fun setThumbnailGallery(uri: Uri?) {
        responseThumbnailGallery.postValue(uri)
    }

    fun clearParameters() {
        responseAudioParameters = null
        responseVidoeParameters = null
        respondInfo = null
    }

    fun setGalleryEntityProperties(galleryEntityProperties: GalleryEntityProperties) {
        properties = galleryEntityProperties
    }

    fun setRespondData(data: RespondData) {
        respondInfo = data
    }
}