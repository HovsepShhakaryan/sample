package com.vylo.main.component.kebab.domain.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlockUser(
    @SerializedName("blocked_profile_global_id")
    val globalId: String? = null,
) : Parcelable
