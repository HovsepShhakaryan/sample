package com.vylo.main.globalsearchmain.searchstartscreen.domain.entiry.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrendingData(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("search_string")
    val searchString: String? = null
) : Parcelable