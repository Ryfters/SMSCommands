package com.smscommands.app.ui.screens.history

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.commands.CommandList
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.utils.formatRelativeTime
import com.smscommands.app.utils.getStatusRes

@Composable
fun HistoryItemDialog(
    navController: NavController,
    viewModel: UiStateViewModel,
    itemId: Int
) {

    val context = LocalContext.current

    val history by viewModel.history.collectAsState()

    val item = history.find { it.id == itemId } ?: run {
        navController.popBackStack()
        Log.e("HistoryItemDialog", "Item not found")
        return
    }

    val commandName = CommandList.find { it.id == item.commandId }?.label
        ?: R.string.screen_history_item_invalid_command

    val formattedTime = formatRelativeTime(context, item.time).replaceFirstChar { it.uppercase() }

    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = { Text(text = "History") },
        text = {
            Column {
                Text("Command: ${stringResource(commandName)}")
                Text("Status: ${getStatusRes(item.status)}")
                Text("Sender: ${item.sender}")
                Text("Time: $formattedTime")
                Text("Message: ${item.message}")
            }
        },
        confirmButton = {
            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    val uri = "smsto:${item.sender}".toUri()
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = uri
                    }
                    context.startActivity(intent)
                }
            ) {
                Text("Jump to conversation")
            }
        }
    )
}