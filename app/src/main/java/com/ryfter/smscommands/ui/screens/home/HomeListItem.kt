package com.ryfter.smscommands.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
    if (topDivider) HorizontalDivider(Modifier.padding(horizontal = 24.dp))
    ListItem(
        headlineContent = { Text(headline, style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)) },
        supportingContent = { content?.let { Text(it) } },
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
    )
}