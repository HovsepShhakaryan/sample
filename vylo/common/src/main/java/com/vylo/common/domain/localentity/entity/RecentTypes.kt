package com.vylo.common.domain.localentity.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vylo.common.domain.localentity.TypeConverterString

@Entity(tableName = "recent_trending_type")
data class RecentTypes(
    @TypeConverters(TypeConverterString::class)
    var recent: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


