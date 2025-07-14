package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.Routes
import com.ryfter.smscommands.ui.screens.settings.DarkThemeDialog
import com.ryfter.smscommands.ui.screens.settings.DismissNotificationsDialog
import com.ryfter.smscommands.ui.screens.settings.SettingsScreen
import com.ryfter.smscommands.ui.screens.settings.TestSmsDialog

fun NavGraphBuilder.settings(navController: NavHostController, viewModel: UiStateViewModel) {
    
    composable(Routes.Settings.MAIN) {
        SettingsScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
    
    dialog(Routes.Settings.TEST_SMS_DIALOG) {
        TestSmsDialog(
            navController = navController
        )
    }
    
    dialog(Routes.Settings.DARK_THEME_DIALOG) {
        DarkThemeDialog(
            navController = navController,
            viewModel = viewModel
        )
    }
    
    dialog(Routes.Settings.DISMISS_NOTIFICATIONS_DIALOG) {
        DismissNotificationsDialog(
            navController = navController,
            viewModel = viewModel
        )
    }
}