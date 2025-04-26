package com.smscommands.app.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val ACCESS_PIN = stringPreferencesKey("access_pin")
    val COLLECT_HISTORY = booleanPreferencesKey("collect_history")
    val DARK_THEME = intPreferencesKey("dark_theme")
    val DISMISS_NOTIFICATION_TYPE = intPreferencesKey("dismiss_notification_type")
    val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    val IS_FIRST_LAUNCH = booleanPreferencesKey("show_onboarding")
    val REQUIRE_PIN = booleanPreferencesKey("require_pin")
}