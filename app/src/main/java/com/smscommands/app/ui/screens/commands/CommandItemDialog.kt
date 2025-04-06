package com.smscommands.app.ui.screens.commands

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.commands.CommandList
import com.smscommands.app.data.UiStateViewModel

@Composable
fun CommandItemDialog(
    navController: NavController,
    viewModel: UiStateViewModel,
    commandId: String
) {

    Log.d("Here", "CommandItemDialog: $commandId")

    val command = CommandList.find { it.id == commandId } ?: run {
        Log.e("CommandItemDialog", "Command not found: $commandId")
        navController.popBackStack()
        return
    }

    Log.d("Here", "CommandItemDialog: $commandId")

    val isEnabled = viewModel.commandPreferences.collectAsState().value[commandId]

    // TODO: Add permission to status
    val status = if (isEnabled == true) stringResource(R.string.common_enabled)
    else stringResource(R.string.common_disabled)

    @Suppress("SimplifiableCallChain") // Doing this makes it so stringResource doesnt work
    val requiredPermissions = command.requiredPermissions
        .map { stringResource(it.label) }
        .joinToString()
        .takeIf { it.isNotEmpty() } ?: stringResource(R.string.common_none)

    val usage = stringResource(command.usage)


    AlertDialog(
        title = {
            Column {
                Text(stringResource(command.label))
                Text(
                    text = stringResource(command.desc),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        text = {
            Column {
                // TODO: Implement permission ViewModel here
                Text(
                    stringResource(R.string.screen_commands_status, status)
                )
                Text(
                    stringResource(
                        R.string.screen_commands_permissions_required,
                        requiredPermissions
                    )
                )
                Text(
                    stringResource(R.string.screen_commands_usage, usage)
                )
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