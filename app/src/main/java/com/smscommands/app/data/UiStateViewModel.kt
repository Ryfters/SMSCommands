package com.smscommands.app.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.smscommands.app.commands.Command
import com.smscommands.app.data.db.HistoryItem
import com.smscommands.app.data.db.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UiStateViewModel(
    private val dataStore: DataStore<Preferences>,
    private val database: HistoryRepository,
): ViewModel() {

    // CommandPreferences
    val commandPreferences: StateFlow<Map<String, Boolean>> =
        dataStore.data.map { preferences ->
            Command.LIST.associate { command ->
                val key = booleanPreferencesKey(command.id)
                command.id to (preferences[key] ?: Defaults.COMMANDS)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    fun updateCommandPreference(id: String, isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[booleanPreferencesKey(id)] = isEnabled
            }
        }
    }

    // Pin
    val pin: StateFlow<String> =
        dataStore.data.map { preferences ->
            preferences[PreferenceKeys.ACCESS_PIN] ?: Defaults.ACCESS_PIN
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Defaults.ACCESS_PIN
        )

    fun updatePin(newPin: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.ACCESS_PIN] = newPin
            }
        }
    }

    // Settings
    // Dynamic color
    val dynamicColorsEnabled: StateFlow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[PreferenceKeys.DYNAMIC_COLOR] ?: Defaults.DYNAMIC_COLOR
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Defaults.DYNAMIC_COLOR
        )

    fun updateDynamicColorsEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.DYNAMIC_COLOR] = isEnabled
            }
        }
    }

    // Dark theme
    val darkThemeType: StateFlow<Int> =
        dataStore.data.map { preferences ->
            preferences[PreferenceKeys.DARK_THEME] ?: Defaults.DARK_THEME
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Defaults.DARK_THEME
        )

    fun updateDarkThemeType(value: Int) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.DARK_THEME] = value
            }
        }
    }

    // History
    val historyEnabled: StateFlow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[PreferenceKeys.COLLECT_HISTORY] ?: Defaults.COLLECT_HISTORY
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Defaults.COLLECT_HISTORY
        )

    fun updateHistoryEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.COLLECT_HISTORY] = isEnabled
            }
        }
    }

    // Dismiss Message's notifications
    val dismissNotificationType: StateFlow<Int> =
        dataStore.data.map { preferences ->
            preferences[PreferenceKeys.DISMISS_NOTIFICATION_TYPE] ?: Defaults.DISMISS_NOTIFICATION_TYPE
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Defaults.DISMISS_NOTIFICATION_TYPE
        )

    fun updateDismissNotificationType(value: Int) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.DISMISS_NOTIFICATION_TYPE] = value
            }
        }
    }

    // isFirstLaunch
    val isFirstLaunch: StateFlow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[PreferenceKeys.IS_FIRST_LAUNCH] ?: Defaults.IS_FIRST_LAUNCH
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun updateIsFirstLaunch(value: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.IS_FIRST_LAUNCH] = value
            }
        }
    }

    // History
    val history: StateFlow<List<HistoryItem>> =
        database.getHistory().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun deleteHistory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteAll()
            }
        }
    }


    companion object {

        val DATASTORE_KEY = object : CreationExtras.Key<DataStore<Preferences>> {}
        val DATABASE_KEY = object : CreationExtras.Key<HistoryRepository> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val dataStore = this[DATASTORE_KEY]
                if (dataStore == null) {
                    throw IllegalStateException("Missing dataStore parameter in data.UiStateViewModel.Factory")
                }

                val database = this[DATABASE_KEY]
                if (database == null) {
                    throw IllegalStateException("Missing database parameter in data.UiStateViewModel.Factory")
                }

                UiStateViewModel(
                    dataStore = dataStore,
                    database = database
                )
            }
        }
    }
}


