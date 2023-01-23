package com.vylo.common.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.vylo.common.domain.localentity.entity.RecentTypes

@Dao
interface RecentTypeDao {

    @Insert
    suspend fun insert(recentTrendingTypes: RecentTypes)

    @Query("DELETE FROM recent_trending_type")
    suspend fun deleteAll()

    @Query("SELECT * FROM recent_trending_type")
    suspend fun getRecentTypes(): List<RecentTypes>
}