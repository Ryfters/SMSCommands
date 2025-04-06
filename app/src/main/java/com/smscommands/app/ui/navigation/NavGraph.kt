package com.smscommands.app.ui.navigation

import android.content.Context
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.screens.commands.CommandItemDialog
import com.smscommands.app.ui.screens.commands.CommandsScreen
import com.smscommands.app.ui.screens.history.HistoryItemDialog
import com.smscommands.app.ui.screens.history.HistoryScreen
import com.smscommands.app.ui.screens.home.EditPinDialog
import com.smscommands.app.ui.screens.home.HomeScreen
import com.smscommands.app.ui.screens.onboarding.OnboardingScreen
import com.smscommands.app.ui.screens.perms.DeclineWarningDialog
import com.smscommands.app.ui.screens.perms.PermsScreen
import com.smscommands.app.ui.screens.settings.DarkThemeDialog
import com.smscommands.app.ui.screens.settings.DismissNotificationsDialog
import com.smscommands.app.ui.screens.settings.SettingsScreen

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userPrefs")

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: UiStateViewModel
) {

    val isFirstLaunch by viewModel.isFirstLaunch.collectAsState()

    val startDestination =
        if (isFirstLaunch) Routes.ONBOARDING
        else Routes.HOME


    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
    ) {

        // Main screen
        composable(Routes.HOME) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Routes.COMMANDS) {
            CommandsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Routes.HISTORY) {
            HistoryScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Routes.PERMISSIONS) {
            PermsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        // Onboarding
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        // Settings
        composable(Routes.SETTINGS) {
            SettingsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // Dialogs
        dialog(Routes.EDIT_PIN_DIALOG) {
            EditPinDialog(
                navController = navController,
                viewModel = viewModel
            )
        }
        dialog(Routes.DARK_THEME_DIALOG) {
            DarkThemeDialog(
                navController = navController,
                viewModel = viewModel
            )
        }
        dialog(Routes.DISMISS_NOTIFICATIONS_DIALOG) {
            DismissNotificationsDialog(
                navController = navController,
                viewModel = viewModel
            )
        }
        dialog(Routes.DECLINE_WARNING_DIALOG) {
            DeclineWarningDialog(
                navController = navController
            )
        }

        dialog(Routes.HISTORY_ITEM_DIALOG + "{itemId}", listOf(navArgument("itemId") { type = NavType.IntType })) {
            val itemId = it.arguments?.getInt("itemId") ?: 0
            HistoryItemDialog(
                navController = navController,
                viewModel = viewModel,
                itemId = itemId
            )
        }
        dialog(Routes.COMMAND_ITEM_DIALOG + "{commandId}", listOf(navArgument("commandId") { type = NavType.StringType })) {
            val commandId = it.arguments?.getString("commandId") ?: ""
            CommandItemDialog(
                navController = navController,
                viewModel = viewModel,
                commandId = commandId
            )
        }
    }
}