package com.ryfter.smscommands.data.db

import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val itemDao: HistoryDao) {
    suspend fun insert(item: HistoryItem): Long = itemDao.insert(item)

    suspend fun updateItemStatus(id: Long, status: Int) = itemDao.updateItemStatus(id, status)

    suspend fun updateItemMessages(id: Long, messages: List<String>) = itemDao.updateItemMessages(id, messages)

    suspend fun getHistoryItem(id: Long): HistoryItem = itemDao.getHistoryItem(id)

    fun getHistory(): Flow<List<HistoryItem>> = itemDao.getHistory()

    fun deleteAll() = itemDao.deleteAll()
}