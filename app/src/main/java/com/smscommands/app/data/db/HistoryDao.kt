package com.smscommands.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: HistoryItem)

    @Query("DELETE FROM command_history")
    fun deleteAll()

    @Query("SELECT * FROM command_history ORDER BY time DESC")
    fun getHistory(): Flow<List<HistoryItem>>
}