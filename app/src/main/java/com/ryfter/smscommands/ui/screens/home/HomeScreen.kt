package com.ryfter.smscommands.ui.screens.home

import android.content.Context
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
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.Command
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.data.db.HistoryItem
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.components.MainScaffold
import com.ryfter.smscommands.ui.navigation.Routes
import com.ryfter.smscommands.utils.formatRelativeTime

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
        actions = { SettingsIcon(onClick = { navController.navigate(Routes.Settings.MAIN) }) },
    ) {
        val totalCommandsCount = Command.LIST.count()
        val commandPreferences by viewModel.commandPreferences.collectAsState()
        val enabledCommandsCount = Command.LIST.count { command ->
            commandPreferences[command.id] == true
        }

        val permissionsState by viewModel.permissionsState.collectAsState()
        val errorCount = Command.LIST.count { command ->
            commandPreferences[command.id] == true &&
                (command.requiredPermissions + Permission.BASE).any { permission ->
                    permissionsState[permission.id] == false
                }
        }

        val errorContent =
            if (errorCount > 0) {
                stringResource(R.string.common_separator) +
                stringResource(R.string.screen_home_commands_errors, errorCount)
            } else ""

        val commandContent = stringResource(
            R.string.screen_home_enabled_count,
            enabledCommandsCount,
            totalCommandsCount
        ) + errorContent


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

        val permissions by viewModel.permissionsState.collectAsState()
        val missingPerms = permissions.count { !it.value }
        val totalPerms = Permission.ALL.count()
        val permissionContent =
                 if (missingPerms == 0) stringResource(R.string.screen_home_perms_all)
            else if (missingPerms == totalPerms) stringResource(R.string.screen_home_perms_none)
            else if (missingPerms == 1) stringResource(R.string.screen_home_perms_one)
            else stringResource(R.string.screen_home_perms_many, missingPerms)

        HomeListItem(
            headline = stringResource(R.string.screen_commands_title),
            content = commandContent,
            onClick = {
                navController.navigate(Routes.Commands.MAIN)
            }
        )
        HomeListItem(
            headline = stringResource(R.string.screen_history_title),
            content = historyContent,
            onClick = {
                navController.navigate(Routes.History.MAIN)
            }
        )
        HomeListItem(
            headline = stringResource(R.string.screen_home_pin),
            content = pinContent,
            onClick = {
                navController.navigate(Routes.Home.EDIT_PIN_DIALOG)
            }
        )
        HomeListItem(
            headline = stringResource(R.string.screen_perms_title),
            content = permissionContent,
            onClick = {
                navController.navigate(Routes.Perms.MAIN)
            }
        )
        HorizontalDivider()
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
            painter = painterResource(R.drawable.ic_settings),
            contentDescription = null
        )
    }
}
