package com.smscommands.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(InstantConverter::class)
@Database(entities = [HistoryItem::class], version = 4, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var Instance: HistoryDatabase? = null

        fun getDatabase(context: Context): HistoryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HistoryDatabase::class.java, "history_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}