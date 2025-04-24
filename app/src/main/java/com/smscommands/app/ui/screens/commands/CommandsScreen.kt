package com.smscommands.app.ui.screens.commands

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.commands.Command
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.permissions.Permission
import com.smscommands.app.ui.components.MainScaffold
import com.smscommands.app.ui.components.MyListItem
import com.smscommands.app.ui.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandsScreen(
    navController: NavController,
    viewModel: UiStateViewModel
) {
    MainScaffold(
        navController = navController,
        title = stringResource(R.string.screen_commands_title),
        showUpButton = true
    ) {
        val commandPreferences by viewModel.commandPreferences.collectAsState()
        val permissionsState by viewModel.permissionsState.collectAsState()
        LazyColumn {
            items(Command.LIST) { command ->
                val missingPermissions = (command.requiredPermissions + Permission.BASE)
                    .filter { permission -> permissionsState[permission.id] == false }

                val disabled = missingPermissions.isNotEmpty()

                @Suppress("SimplifiableCallChain")
                val content =
                    if (disabled) {
                        val missingPermsString = missingPermissions.map { stringResource(it.label) }.joinToString()
                        stringResource(R.string.screen_commands_missing_permissions, missingPermsString)
                    } else stringResource(command.description)


                MyListItem(
                    title = stringResource(command.label),
                    content = content,
                    onClick = {
                        navController.navigate(Routes.COMMAND_ITEM_DIALOG + command.id)
                    },
                    separator = true,
                    action = {
                        Switch(
                            checked = commandPreferences[command.id] == true,
                            onCheckedChange = { value ->
                                viewModel.updateCommandPreference(command.id, value)
                            },
                            enabled = !disabled
                        )
                    },
                )
            }
        }
    }
}