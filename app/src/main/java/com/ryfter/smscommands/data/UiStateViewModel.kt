package com.ryfter.smscommands.data

import android.content.Context
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
import com.ryfter.smscommands.commands.Command
import com.ryfter.smscommands.data.db.HistoryItem
import com.ryfter.smscommands.data.db.HistoryRepository
import com.ryfter.smscommands.permissions.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UiStateViewModel(
    private val dataStore: DataStore<Preferences>,
    val database: HistoryRepository,
): ViewModel() {

    // Pin
    val pin: StateFlow<String> = getStateFlow(Pref.ACCESS_PIN)
    fun updatePin(value: String) = updatePreference(Pref.ACCESS_PIN, value)

    // Settings
    val dynamicColorsEnabled: StateFlow<Boolean> = getStateFlow(Pref.DYNAMIC_COLOR)
    fun updateDynamicColorsEnabled(value: Boolean) = updatePreference(Pref.DYNAMIC_COLOR, value)

    val darkThemeType: StateFlow<Int> = getStateFlow(Pref.DARK_THEME)
    fun updateDarkThemeType(value: Int) = updatePreference(Pref.DARK_THEME, value)

    val historyEnabled: StateFlow<Boolean> = getStateFlow(Pref.COLLECT_HISTORY)
    fun updateHistoryEnabled(value: Boolean) = updatePreference(Pref.COLLECT_HISTORY, value)

    val dismissNotificationType: StateFlow<Int> = getStateFlow(Pref.DISMISS_NOTIFICATION_TYPE)
    fun updateDismissNotificationType(value: Int) =
        updatePreference(Pref.DISMISS_NOTIFICATION_TYPE, value)

    val requirePin: StateFlow<Boolean> = getStateFlow(Pref.REQUIRE_PIN)
    fun updateRequirePin(value: Boolean) = updatePreference(Pref.REQUIRE_PIN, value)

    val lastBuildNumber: StateFlow<Int> = getStateFlow(Pref.LAST_BUILD_CODE)
    fun updateLastBuildCode(value: Int) = updatePreference(Pref.LAST_BUILD_CODE, value)


    // CommandPreferences
    val commandPreferences: StateFlow<Map<String, Boolean>> =
        dataStore.data.map { preferences ->
            Command.LIST.associate { command ->
                val key = booleanPreferencesKey(command.id)
                command.id to (preferences[key] ?: Pref.COMMANDS.defaultValue!!)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    fun updateCommandPreference(id: String, value: Boolean) =
        updatePreference(Pref(booleanPreferencesKey(id), null), value)

    // Permissions
    private val _permissionsState = MutableStateFlow(Permission.ALL.associate { it.id to false} )
    val permissionsState: StateFlow<Map<String, Boolean>> = _permissionsState

    fun updateSinglePermissionState(permissionId: String, isEnabled: Boolean) {
        _permissionsState.update { permissions ->
            permissions + (permissionId to isEnabled)
        }
    }

    fun refreshPermissionsState(context: Context) {
        viewModelScope.launch {
            _permissionsState.update {
                Permission.ALL.associate { it.id to it.isGranted(context) }
            }
        }
    }

    // signedIn
    private val _signedIn = MutableStateFlow(false)
    val signedIn: StateFlow<Boolean> = _signedIn
    fun updateSignedIn(value: Boolean) = _signedIn.update { value }


    // History
    val history: StateFlow<List<HistoryItem>> =
        database.getHistory().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun clearHistory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.deleteAll()
            }
        }
    }

    private fun <T> UiStateViewModel.getStateFlow(pref: Pref<T>): StateFlow<T> {
        return dataStore.data
            .map { preferences -> preferences[pref.preferenceKey!!] ?: pref.defaultValue!! }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = pref.defaultValue!!
            )
    }

    private fun <T> UiStateViewModel.updatePreference(pref: Pref<T>, value: T) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[pref.preferenceKey!!] = value
            }
        }
    }


    companion object {

        val DATASTORE_KEY = object : CreationExtras.Key<DataStore<Preferences>> {}
        val DATABASE_KEY = object : CreationExtras.Key<HistoryRepository> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val dataStore = this[DATASTORE_KEY]
                if (dataStore == null) throw IllegalStateException("Missing dataStore parameter in data.UiStateViewModel.Factory")

                val database = this[DATABASE_KEY]
                if (database == null) throw IllegalStateException("Missing database parameter in data.UiStateViewModel.Factory")

                UiStateViewModel(dataStore, database)
            }
        }
    }
}