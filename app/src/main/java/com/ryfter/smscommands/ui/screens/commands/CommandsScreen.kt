package com.ryfter.smscommands.ui.screens.commands

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.Command
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.components.MainScaffold
import com.ryfter.smscommands.ui.components.MyListItem
import com.ryfter.smscommands.ui.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandsScreen(
    navController: NavController,
    viewModel: UiStateViewModel
) {
    val context = LocalContext.current

    MainScaffold(
        navController = navController,
        title = stringResource(R.string.screen_commands_title),
        showUpButton = true,
    ) {
        val commandPreferences by viewModel.commandPreferences.collectAsState()
        val permissionsState by viewModel.permissionsState.collectAsState()

        Command.LIST.forEach { command ->
            val missingPermissions = (command.requiredPermissions + Permission.BASE)
                .filter { permission -> permissionsState[permission.id] == false }

            val disabled = missingPermissions.isNotEmpty()

            val content = if (disabled) stringResource(
                R.string.screen_commands_missing_permissions,
                missingPermissions.joinToString { context.getString(it.label) }
            ) else stringResource(command.description)


            MyListItem(
                title = stringResource(command.label),
                content = content,
                onClick = { navController.navigate(Routes.Commands.ITEM + command.id) },
                separator = true,
                action = {
                    Switch(
                        checked = commandPreferences[command.id] == true,
                        onCheckedChange = { value ->
                            viewModel.updateCommandPreference(command.id, value)
                        },
                        enabled = !disabled
                    )
                }
            )
        }
    }
}