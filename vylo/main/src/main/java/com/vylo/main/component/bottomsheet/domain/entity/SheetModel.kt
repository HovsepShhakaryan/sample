package com.vylo.main.component.bottomsheet.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SheetModel(
    var imageId: Int,
    var name: String
) : Parcelable
