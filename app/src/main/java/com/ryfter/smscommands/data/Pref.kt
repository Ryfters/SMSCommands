package com.ryfter.smscommands.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class Pref<T>(
    val preferenceKey: Preferences.Key<T>?,
    val defaultValue: T?
) {
    companion object {
        val ACCESS_PIN = Pref(
            preferenceKey = stringPreferencesKey("access_pin"),
            defaultValue = ""
        )
        val COLLECT_HISTORY = Pref(
            preferenceKey = booleanPreferencesKey("collect_history"),
            defaultValue = true
        )
        val COMMANDS = Pref(
            preferenceKey = null,
            defaultValue = false
        )
        val DARK_THEME = Pref(
            preferenceKey = intPreferencesKey("dark_theme"),
            defaultValue = 0
        )
        val DISMISS_NOTIFICATION_TYPE = Pref(
            preferenceKey = intPreferencesKey("dismiss_notification_type"),
            defaultValue = 1
        )
        val DYNAMIC_COLOR = Pref(
            preferenceKey = booleanPreferencesKey("dynamic_color"),
            defaultValue = true
        )
        val LAST_BUILD_CODE = Pref(
            preferenceKey = intPreferencesKey("last_build_number"),
            defaultValue = -1
        )
        val REQUIRE_PIN = Pref(
            preferenceKey = booleanPreferencesKey("require_pin"),
            defaultValue = false
        )
    }
}





