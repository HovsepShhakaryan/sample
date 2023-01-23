package com.vylo.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vylo.common.domain.localentity.entity.PublishersEntity

@Dao
interface PublisherDao {

    @Insert
    suspend fun saveAll(list: List<PublishersEntity>)

    @Insert
    suspend fun save(item: PublishersEntity)

    @Query("SELECT * FROM publishers_entity")
    suspend fun getAllPublishers(): List<PublishersEntity>?

    @Query("DELETE FROM publishers_entity")
    suspend fun deleteAll()

    @Query("DELETE FROM publishers_entity WHERE id = :id")
    suspend fun delete(id: Long)
}