package com.smscommands.app.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.components.RadioItem

@Composable
fun DismissNotificationsDialog(
    navController: NavController,
    viewModel: UiStateViewModel,
) {
    val dismissNotificationType by viewModel.dismissNotificationType.collectAsState()

    AlertDialog(
        title = {
            Column {
                Text(stringResource(R.string.screen_settings_dismiss_dialog_title))
                Text(
                    text = stringResource(R.string.screen_settings_dismiss_desc),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.selectableGroup()
            ) {
                RadioItem(
                    text = stringResource(R.string.screen_settings_dismiss_mark_as_read),
                    selected = dismissNotificationType == 2,
                    onSelected = {
                        viewModel.updateDismissNotificationType(2)
                        navController.popBackStack()
                    }
                )
                RadioItem(
                    text = stringResource(R.string.screen_settings_dismiss_only),
                    selected = dismissNotificationType == 1,
                    onSelected = {
                        viewModel.updateDismissNotificationType(1)
                        navController.popBackStack()
                    }
                )
                RadioItem(
                    text = stringResource(R.string.screen_settings_dismiss_disable),
                    selected = dismissNotificationType == 0,
                    onSelected = {
                        viewModel.updateDismissNotificationType(0)
                        navController.popBackStack()
                    }
                )
            }
        },
        onDismissRequest = { navController.popBackStack() },
        confirmButton = {}
    )
}