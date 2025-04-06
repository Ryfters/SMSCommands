package com.smscommands.app.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeListItem(
    headline: String,
    content: String?,
    onClick: () -> Unit,
    topDivider: Boolean = true,
) {
    if (topDivider) HorizontalDivider()
    ListItem(
        headlineContent = { Text(headline, style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)) },
        supportingContent = { content?.let { Text(it) } },
        trailingContent = {
            IconButton(
                onClick = onClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = headline,
                )
            }
        },
        modifier = Modifier.padding(vertical = 4.dp),
    )
}