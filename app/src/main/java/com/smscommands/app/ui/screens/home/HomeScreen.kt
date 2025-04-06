package com.smscommands.app.ui.screens.home

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.commands.CommandList
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.data.db.HistoryItem
import com.smscommands.app.ui.components.MainScaffold
import com.smscommands.app.ui.navigation.Routes
import com.smscommands.app.utils.formatRelativeTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: UiStateViewModel
) {
    val context: Context = LocalContext.current

    MainScaffold(
        navController = navController,
        title = stringResource(R.string.screen_home_title),
        actions = { SettingsIcon(onClick = { navController.navigate(Routes.SETTINGS) }) },
    ) {

        val totalCommandsCount = CommandList.count()
        val commandPreferences by viewModel.commandPreferences.collectAsState()
        val enabledCommandsCount = commandPreferences.count { command -> command.value }
        val commandContent = stringResource(
            R.string.screen_home_enabled_count,
            enabledCommandsCount,
            totalCommandsCount
        )


        val historyEnabled by viewModel.historyEnabled.collectAsState()
        val lastUsedCommand: HistoryItem? = viewModel.history.collectAsState().value.getOrNull(0)
        val historyContent = if (!historyEnabled) {
            stringResource(R.string.screen_home_history_disabled)
        } else {
            lastUsedCommand?.let {
                val time = formatRelativeTime(context, it.time)
                stringResource(R.string.screen_home_history_last_executed, time)
            } ?: stringResource(R.string.screen_home_history_empty)
        }

        val pin by viewModel.pin.collectAsState()
        val pinContent = if (pin.isEmpty()) {
            stringResource(R.string.screen_home_pin_not_set)
        } else {
            stringResource(R.string.screen_home_pin_current, pin)
        }

        Column {
            HomeListItem(
                headline = stringResource(R.string.screen_commands_title),
                content = commandContent,
                onClick = {
                    navController.navigate(Routes.COMMANDS)
                }
            )
            HomeListItem(
                headline = stringResource(R.string.screen_history_title),
                content = historyContent,
                onClick = {
                    navController.navigate(Routes.HISTORY)
                }
            )
            HomeListItem(
                headline = stringResource(R.string.screen_home_pin),
                content = pinContent,
                onClick = {
                    navController.navigate(Routes.EDIT_PIN_DIALOG)
                }
            )
            HomeListItem(
                headline = stringResource(R.string.screen_perms_title),
                content = null,
                onClick = {
                    navController.navigate(Routes.PERMISSIONS)
                }
            )
            HorizontalDivider()
        }
    }
}


@Composable
fun SettingsIcon(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = null
        )
    }
}
