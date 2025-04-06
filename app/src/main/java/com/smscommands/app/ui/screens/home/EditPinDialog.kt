package com.smscommands.app.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.data.UiStateViewModel

@Composable
fun EditPinDialog(
    navController: NavController,
    viewModel: UiStateViewModel,
) {
    var showNoPinDialog by remember { mutableStateOf(false) }

    if (!showNoPinDialog) {
        EditPinDialog(
            navController = navController,
            viewModel = viewModel,
            onRemovePin = { showNoPinDialog = true },
        )
    } else {
        NoPinWarningDialog(
            navController = navController,
            viewModel = viewModel,
            onCancel = { showNoPinDialog = false },
        )
    }
}

@Composable
private fun EditPinDialog(
    navController: NavController,
    viewModel: UiStateViewModel,
    onRemovePin: () -> Unit,
) {
    var editedPinValue by remember { mutableStateOf("")}

    AlertDialog(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.screen_home_pin_edit_pin))
            }
        },
        text = {
            OutlinedTextField(
                value = editedPinValue,
                label = { Text(stringResource(R.string.screen_home_pin_new_pin)) },
                onValueChange = { editedPinValue = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (editedPinValue.isEmpty()) {
                        onRemovePin()
                    } else {
                        viewModel.updatePin(editedPinValue)
                        navController.popBackStack()
                    }
                },
            ) {
                if (editedPinValue.isEmpty()) {
                    Text(stringResource(R.string.screen_home_pin_remove))
                } else {
                    Text(stringResource(R.string.common_confirm))
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(stringResource(R.string.common_cancel))
            }
        },
        onDismissRequest = {
            navController.popBackStack()
        }
    )
}

@Composable
private fun NoPinWarningDialog(
    navController: NavController,
    viewModel: UiStateViewModel,
    onCancel: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(stringResource(R.string.common_are_you_sure))
        },
        text = {
            Text(stringResource(R.string.screen_home_pin_remove_rationale))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.updatePin("")
                    navController.popBackStack()
                }
            ) {
                Text(stringResource(R.string.screen_home_pin_remove))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(stringResource(R.string.common_cancel))
            }
        },
        onDismissRequest = {
            navController.popBackStack()
        }
    )
}