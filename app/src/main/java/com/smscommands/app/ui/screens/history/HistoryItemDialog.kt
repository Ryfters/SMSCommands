package com.smscommands.app.ui.screens.history

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.commands.Command
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.components.MyTextButton
import com.smscommands.app.utils.formatRelativeTime

@Composable
fun HistoryItemDialog(
    navController: NavController,
    viewModel: UiStateViewModel,
    itemId: Long
) {
    val context = LocalContext.current

    val history by viewModel.history.collectAsState()

    val item = history.find { it.id == itemId } ?: run {
        navController.popBackStack()
        Log.e("HistoryItemDialog", "Item not found")
        return
    }

    val commandName = Command.LIST.find { it.id == item.commandId }?.label
        ?: R.string.command_status_invalid_command

    val formattedTime = formatRelativeTime(context, item.time).replaceFirstChar { it.uppercase() }

    val commandContent = stringResource(R.string.screen_history_item_dialog_command, stringResource(commandName))
    val statusContent = stringResource(R.string.screen_history_item_dialog_status, stringResource(item.status))
    val senderContent = stringResource(R.string.screen_history_item_dialog_sender, item.sender)
    val timeContent = stringResource(R.string.screen_history_item_dialog_time, formattedTime)
    val messageContent = stringResource(R.string.screen_history_item_dialog_message, item.trigger)

    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = { Text(text = "History") },
        text = {
            Column {
                Text(commandContent)
                Text(statusContent)
                Text(senderContent)
                Text(timeContent)
                Text(messageContent)
            }
        },
        confirmButton = {
            MyTextButton(stringResource(R.string.common_ok)) { navController.popBackStack() }
        },
        dismissButton = {
            MyTextButton(stringResource(R.string.screen_history_item_dialog_jump_to_conv)) {
                val uri = "smsto:${item.sender}".toUri()
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = uri
                }
                context.startActivity(intent)
            }
        }
    )
}