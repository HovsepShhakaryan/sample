
package com.vylo.common.entity
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadData(
    val id: String,
    val url: String,
    val title: String,
    val categoryId: String,
    val categoryName: String,
    val responseToGlobalId: String? = null,
    val responseToTitle: String? = null,
    val responseToCategoryId: String? = null,
    val responseToCategoryName: String? = null,
    val status: Int? = null
) : Parcelable