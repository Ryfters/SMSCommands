package com.ryfter.smscommands.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: HistoryItem): Long

    @Query("UPDATE command_history SET status = :status WHERE id = :id")
    suspend fun updateItemStatus(id: Long, status: Int)

    @Query("UPDATE command_history SET messages = :messages WHERE id = :id")
    suspend fun updateItemMessages(id: Long, messages: List<String>)

    @Query("SELECT * FROM command_history WHERE id = :id")
    suspend fun getHistoryItem(id: Long): HistoryItem

    @Query("SELECT * FROM command_history ORDER BY time DESC")
    fun getHistory(): Flow<List<HistoryItem>>

    @Query("DELETE FROM command_history")
    fun deleteAll()
}