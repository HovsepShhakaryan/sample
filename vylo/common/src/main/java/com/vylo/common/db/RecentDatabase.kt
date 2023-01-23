package com.vylo.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vylo.common.db.dao.PublisherDao
import com.vylo.common.db.dao.RecentTypeDao
import com.vylo.common.domain.localentity.TypeConverterString
import com.vylo.common.domain.localentity.entity.PublishersEntity
import com.vylo.common.domain.localentity.entity.RecentTypes

@Database(
    entities = [
        RecentTypes::class,
        PublishersEntity::class
    ], version = 2
)
@TypeConverters(TypeConverterString::class)
abstract class RecentDatabase : RoomDatabase() {
    abstract fun recentDao(): RecentTypeDao
    abstract fun publisherDao(): PublisherDao

    companion object {
        @Volatile
        private var INSTANCE: RecentDatabase? = null

        fun getDatabase(context: Context): RecentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecentDatabase::class.java,
                    "recent_trending_type"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}