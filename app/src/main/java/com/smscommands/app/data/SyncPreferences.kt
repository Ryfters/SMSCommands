package com.smscommands.app.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.smscommands.app.data.db.HistoryItem
import com.smscommands.app.data.db.HistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant

class SyncPreferences(
    val dataStore: DataStore<Preferences>,
    val database: HistoryRepository?,
) {
    fun readPin(): String {
        return runBlocking {
            dataStore.data.map { preferences ->
                preferences[PreferenceKeys.ACCESS_PIN] ?: Defaults.ACCESS_PIN
            }.first()
        }
    }

    fun readCommandEnabled(id: String): Boolean {
        return runBlocking {
            dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey(id)] ?: Defaults.COMMANDS
            }.first()
        }
    }

    fun readDismissNotificationType(): Int {
        return runBlocking {
            dataStore.data.map { preferences ->
                preferences[PreferenceKeys.DISMISS_NOTIFICATION_TYPE] ?: Defaults.DISMISS_NOTIFICATION_TYPE
            }.first()
        }
    }

    fun readHistoryEnabled(): Boolean {
        return runBlocking {
            dataStore.data.map { preferences ->
                preferences[PreferenceKeys.COLLECT_HISTORY] ?: Defaults.COLLECT_HISTORY
            }.first()
        }
    }

    fun saveHistoryItem(
        time: Instant,
        commandId: String,
        status: String,
        sender: String,
        message: String,
    ) {
        if (database == null) throw IllegalStateException("syncPreferences.database was not initialized")
        CoroutineScope(Dispatchers.IO).launch {
            database.insert(
                HistoryItem(
                    time = time,
                    commandId = commandId,
                    status = status,
                    sender = sender,
                    message = message
                )
            )
        }
    }
}