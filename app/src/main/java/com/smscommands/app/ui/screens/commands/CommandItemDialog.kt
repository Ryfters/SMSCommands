package com.smscommands.app.ui.screens.commands

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.commands.Command
import com.smscommands.app.commands.params.FlagParamDefinition
import com.smscommands.app.commands.params.OptionParamDefinition
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.permissions.Permission

@Composable
fun CommandItemDialog(
    navController: NavController,
    viewModel: UiStateViewModel,
    commandId: String
) {
    val context = LocalContext.current

    val command = Command.LIST.find { it.id == commandId } ?: run {
        Log.e("CommandItemDialog", "Command not found: $commandId")
        navController.popBackStack()
        return
    }

    val permissionsState by viewModel.permissionsState.collectAsState()

    val isMissingPerms = (command.requiredPermissions + Permission.REQUIRED).any { permission ->
        permissionsState[permission.id] == true
    }

    val isEnabled = viewModel.commandPreferences.collectAsState().value[commandId] == true

    val status =
        if (isMissingPerms) stringResource(R.string.screen_commands_status_missing_perms)
        else if (isEnabled) stringResource(R.string.common_enabled)
        else stringResource(R.string.common_disabled)

    @Suppress("SimplifiableCallChain") // Doing this makes it so stringResource doesnt work
    val requiredPermissions = command.requiredPermissions
        .map { stringResource(it.label) }
        .joinToString()
        .takeIf { it.isNotEmpty() } ?: stringResource(R.string.common_none)

    val flags = command.params.filter { it.value is FlagParamDefinition }

    @Suppress("UNCHECKED_CAST")
    val options = command.params.filterNot { it.value is FlagParamDefinition }
            as Map<String, OptionParamDefinition>

    AlertDialog(
        title = {
            Column {
                Text(stringResource(command.label))
                Text(
                    text = stringResource(command.description),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        text = {
            Column {
                Text(
                    stringResource(R.string.screen_commands_status, status)
                )
                Text(
                    stringResource(
                        R.string.screen_commands_permissions_required,
                        requiredPermissions
                    )
                )
                // FIXME Make ui better
                if (flags.isEmpty()) {
                    Text("Flags: " + stringResource(R.string.common_none))
                } else {
                    Text("Flags: ")
                    flags.values.forEach { param ->
                        val paramContent = stringResource(
                            R.string.common_bullet_v1_colon_v2,
                            stringResource(param.name),
                            stringResource(param.desc)
                        )
                        Text(paramContent)
                    }
                }

                if (options.isEmpty()) {
                    Text("Options: " + stringResource(R.string.common_none))
                } else {
                    Text("Options: ")
                    options.values.forEach { param ->
                        val paramContent = stringResource(
                            R.string.common_bullet_v1_colon_v2,
                            stringResource(param.name),
                            stringResource(param.desc)
                        )
                        val possibleValues = param.possibleValues(context)
                        Text(paramContent)
                        Text("    Possible values: $possibleValues")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { navController.popBackStack() },
            ) {
                Text(stringResource(R.string.common_ok))
            }
        },
        onDismissRequest = { navController.popBackStack() }
    )
}