package com.smscommands.app.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.navigation.Routes
import com.smscommands.app.ui.screens.settings.DarkThemeDialog
import com.smscommands.app.ui.screens.settings.DismissNotificationsDialog
import com.smscommands.app.ui.screens.settings.SettingsScreen
import com.smscommands.app.ui.screens.settings.TestSmsDialog

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