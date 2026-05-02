package com.example.gullycricket.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gullycricket.core.database.dao.MatchDao
import com.example.gullycricket.core.database.entity.BallEventEntity
import com.example.gullycricket.core.database.entity.MatchEntity

@Database(entities = [MatchEntity::class, BallEventEntity::class, com.example.gullycricket.core.database.entity.PlayerEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gully_cricket_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
