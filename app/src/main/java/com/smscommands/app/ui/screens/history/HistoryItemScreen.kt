package com.smscommands.app.ui.screens.history

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.commands.Command
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.components.MainScaffold
import com.smscommands.app.ui.components.MyListItem
import com.smscommands.app.ui.components.Subtitle
import com.smscommands.app.utils.formatRelativeTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItemScreen(
    navController: NavController,
    viewModel: UiStateViewModel,
    itemId: Long
) {
    MainScaffold(
        navController = navController,
        title = "History",
        showUpButton = true,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        val context = LocalContext.current

        val history by viewModel.history.collectAsState()

        val item = history.find { it.id == itemId } ?: run {
            navController.popBackStack()
            Log.e("HistoryItemDialog", "Item not found")
            return@MainScaffold
        }

        val commandName = Command.LIST.find { it.id == item.commandId }?.label
            ?: R.string.status_invalid_command

        val formattedTime =
            formatRelativeTime(context, item.time).replaceFirstChar { it.uppercase() }


        Subtitle(stringResource(R.string.common_details))
        MyListItem(
            title = stringResource(R.string.screen_history_item_dialog_command),
            content = stringResource(commandName)
        )
        MyListItem(
            title = stringResource(R.string.screen_history_item_dialog_status),
            content = stringResource(item.status)
        )
        MyListItem(
            title = stringResource(R.string.screen_history_item_dialog_sender),
            content = item.sender
        )
        MyListItem(
            title = stringResource(R.string.screen_history_item_dialog_time),
            content = formattedTime
        )

        // TODO: Better
        Subtitle("Conversation")
//            MyListItem(
//                title = stringResource(R.string.screen_history_item_dialog_jump_to_conv),
//
//            )
        MyListItem(
            title = "Conversation",
            action = {
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = "smsto:${item.sender}".toUri()
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_open_in),
                        contentDescription = null
                    )
                }
            }
        )

        HorizontalDivider()
        ChatPreview(
            triggerMessage = item.trigger,
            responses = item.messages
        )
    }
}