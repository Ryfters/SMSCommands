package com.smscommands.app.data.db

import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val itemDao: HistoryDao) {
    suspend fun insert(item: HistoryItem) = itemDao.insert(item)

    fun deleteAll() = itemDao.deleteAll()

    fun getHistory(): Flow<List<HistoryItem>> = itemDao.getHistory()
}