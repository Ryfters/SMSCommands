package com.ryfter.smscommands.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.ryfter.smscommands.data.db.HistoryDatabase
import com.ryfter.smscommands.data.db.HistoryItem
import com.ryfter.smscommands.data.db.HistoryRepository
import com.ryfter.smscommands.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.Instant

class SyncPreferences private constructor(
    private val dataStore: DataStore<Preferences>,
    private val database: HistoryRepository,
) {

    fun readPin(): String = runBlocking {
        dataStore.data.map { preferences ->
            preferences[PreferenceKeys.ACCESS_PIN] ?: Defaults.ACCESS_PIN
        }.first()
    }

    fun readCommandEnabled(id: String): Boolean = runBlocking {
        dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(id)] ?: Defaults.COMMANDS
        }.first()
    }

    fun readDismissNotificationType(): Int = runBlocking {
        dataStore.data.map { preferences ->
            preferences[PreferenceKeys.DISMISS_NOTIFICATION_TYPE] ?: Defaults.DISMISS_NOTIFICATION_TYPE
        }.first()
    }

    fun readHistoryEnabled(): Boolean = runBlocking {
        dataStore.data.map { preferences ->
            preferences[PreferenceKeys.COLLECT_HISTORY] ?: Defaults.COLLECT_HISTORY
        }.first()
    }

    fun saveHistoryItem(
        sender: String,
        commandId: String,
        status: Int,
        trigger: String,
        messages: List<String>,
        time: Instant = Instant.now(),
    ): Long = runBlocking {
        database.insert(
            HistoryItem(
                time = time,
                commandId = commandId,
                status = status,
                sender = sender,
                trigger = trigger,
                messages = messages
            )
        )
    }

    fun updateItemStatus(id: Long, status: Int) = runBlocking {
        database.updateItemStatus(id, status)
    }

    fun addToResponse(id: Long, newMessage: String) = runBlocking {
        val messages = database.getHistoryItem(id).messages
        database.updateItemMessages(id, messages + newMessage)
    }

    companion object {
        @Volatile
        private var Instance: SyncPreferences? = null

        fun getPreferences(context: Context): SyncPreferences {
            return Instance ?: synchronized(this) {
                val dataStore = context.dataStore
                val database = HistoryRepository(HistoryDatabase.getDatabase(context).historyDao())

                SyncPreferences(dataStore, database)
            }
        }
    }
}