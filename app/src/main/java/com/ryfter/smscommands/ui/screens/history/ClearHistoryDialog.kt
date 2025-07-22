package com.ryfter.smscommands.ui.screens.history

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.components.MyTextButton
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.pop


@Composable
fun ClearHistoryDialog(
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel,
) {
    AlertDialog(
        title = { Text(stringResource(R.string.screen_history_clear_title)) },
        text = { Text(stringResource(R.string.screen_history_clear_message)) },
        confirmButton = {
            MyTextButton(stringResource(R.string.screen_history_clear_confirm)) {
                viewModel.clearHistory()
                backStack.pop()
            }
        },
        dismissButton = {
            MyTextButton(stringResource(R.string.common_cancel)) { backStack.pop() }
        },
        onDismissRequest = { backStack.pop() },
    )
}