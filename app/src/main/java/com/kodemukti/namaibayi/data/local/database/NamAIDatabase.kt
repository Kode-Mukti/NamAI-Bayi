package com.kodemukti.namaibayi.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kodemukti.namaibayi.core.common.Constants
import com.kodemukti.namaibayi.data.local.dao.FavoriteDao
import com.kodemukti.namaibayi.data.local.dao.HistoryDao
import com.kodemukti.namaibayi.data.local.entity.FavoriteEntity
import com.kodemukti.namaibayi.data.local.entity.HistoryEntity

@Database(
    entities = [FavoriteEntity::class, HistoryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class NamAIDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: NamAIDatabase? = null

        fun getInstance(context: Context): NamAIDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NamAIDatabase::class.java,
                    Constants.DATABASE_NAME,
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
