package com.smscommands.app.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


/**
 * Shorter TextButton, mostly used to remove clutter in [androidx.compose.material3.AlertDialog]
 */
@Composable
fun MyTextButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(onClick) {
        Text(text, modifier = modifier)
    }
}