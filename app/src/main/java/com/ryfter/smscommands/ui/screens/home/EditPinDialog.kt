package com.ryfter.smscommands.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.components.MyTextButton

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

    val confirmButtonText =
        if (editedPinValue.isEmpty()) stringResource(R.string.screen_home_pin_remove)
        else stringResource(R.string.common_confirm)

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
            MyTextButton(confirmButtonText) {
                if (editedPinValue.isEmpty()) {
                    onRemovePin()
                } else {
                    viewModel.updatePin(editedPinValue)
                    navController.popBackStack()
                }
            }
        },
        dismissButton = {
            MyTextButton(stringResource(R.string.common_cancel)) {
                navController.popBackStack()
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
            Text(stringResource(R.string.screen_home_pin_remove_title))
        },
        text = {
            Text(stringResource(R.string.screen_home_pin_remove_rationale))
        },
        confirmButton = {
            MyTextButton(stringResource(R.string.screen_home_pin_remove)) {
                viewModel.updatePin("")
                navController.popBackStack()
            }
        },
        dismissButton = {
            MyTextButton(stringResource(R.string.common_cancel)) { onCancel }
        },
        onDismissRequest = {
            navController.popBackStack()
        }
    )
}