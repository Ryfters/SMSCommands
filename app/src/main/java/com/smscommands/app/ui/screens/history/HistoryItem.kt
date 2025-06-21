package com.smscommands.app.ui.screens.history

import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.smscommands.app.R
import com.smscommands.app.commands.Command
import com.smscommands.app.data.db.HistoryItem
import com.smscommands.app.ui.components.MyListItem
import com.smscommands.app.utils.formatRelativeTime

@Composable
fun HistoryItem(
    historyItem: HistoryItem,
    onInfoPressed: () -> Unit
) {
    val context: Context = LocalContext.current

    val commandNameRes = Command.LIST.find { command -> command.id == historyItem.commandId }?.label
        ?: R.string.status_invalid_command

    val headline =
        historyItem.sender +
                stringResource(R.string.common_colon) +
                stringResource(commandNameRes)

    val formattedTime = formatRelativeTime(context, historyItem.time)
    val content =
        stringResource(historyItem.status) +
                stringResource(R.string.common_separator) +
        formattedTime

    MyListItem(
        title = headline,
        content = content,
        onClick = onInfoPressed,
        action = {
            IconButton(
                onClick = onInfoPressed
            ) {
                Icon(painterResource(R.drawable.ic_info), null)
            }
        }
    )
}