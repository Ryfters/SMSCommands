package com.smscommands.app.ui.screens.history

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.smscommands.app.R
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.components.MyTextButton


@Composable
fun ClearHistoryDialog(
    navController: NavHostController,
    viewModel: UiStateViewModel,
) {
    AlertDialog(
        title = { Text(stringResource(R.string.screen_history_clear_title)) },
        text = { Text(stringResource(R.string.screen_history_clear_message)) },
        confirmButton = {
            MyTextButton(stringResource(R.string.screen_history_clear_confirm)) {
                viewModel.clearHistory()
                navController.popBackStack()
            }
        },
        dismissButton = {
            MyTextButton(stringResource(R.string.common_cancel)) { navController.popBackStack() }
        },
        onDismissRequest = { navController.popBackStack() },
    )
}