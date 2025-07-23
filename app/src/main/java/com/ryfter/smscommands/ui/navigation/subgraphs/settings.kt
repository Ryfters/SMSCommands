package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation3.runtime.NavEntry
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.dialogNavEntry
import com.ryfter.smscommands.ui.screens.settings.DarkThemeDialog
import com.ryfter.smscommands.ui.screens.settings.DismissNotificationsDialog
import com.ryfter.smscommands.ui.screens.settings.SettingsScreen
import com.ryfter.smscommands.ui.screens.settings.TestSmsDialog

fun settings(
    key: Route.Settings,
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel
): NavEntry<Route> {
    return when (key) {
        is Route.Settings.TestSmsDialog -> dialogNavEntry(key) {
            TestSmsDialog(backStack, viewModel)
        }

        is Route.Settings.DarkThemeDialog -> dialogNavEntry(key) {
            DarkThemeDialog(backStack, viewModel)
        }

        is Route.Settings.DismissNotificationsDialog -> dialogNavEntry(key) {
            DismissNotificationsDialog(backStack, viewModel)
        }

        else -> NavEntry(key) {
            SettingsScreen(backStack, viewModel)
        }
    }
}