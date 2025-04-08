package com.smscommands.app.ui.screens.history

import android.content.Context
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.smscommands.app.R
import com.smscommands.app.commands.CommandList
import com.smscommands.app.data.db.HistoryItem
import com.smscommands.app.ui.components.MyListItem
import com.smscommands.app.utils.formatRelativeTime
import com.smscommands.app.utils.getStatusRes

@Composable
fun HistoryItem(
    historyItem: HistoryItem,
    onInfoPressed: () -> Unit
) {
    val context: Context = LocalContext.current

    val commandNameRes = CommandList.find { command -> command.id == historyItem.commandId }?.label
        ?: R.string.screen_history_item_invalid_command

    val headline =
        stringResource(
            R.string.common_v1_colon_v2,
            historyItem.sender,
            stringResource(commandNameRes)
        )

    val formattedTime = formatRelativeTime(context, historyItem.time)
    val content = stringResource(
        R.string.common_v1_comma_v2,
        stringResource(getStatusRes(historyItem.status)),
        formattedTime
    )

    MyListItem(
        title = headline,
        content = content,
        action = {
            IconButton(
                onClick = onInfoPressed
            ) {
                Icon(painterResource(R.drawable.ic_info), null)
            }
        }
    )
}