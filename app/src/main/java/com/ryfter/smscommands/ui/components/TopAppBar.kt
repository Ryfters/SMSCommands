package com.ryfter.smscommands.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ryfter.smscommands.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBar(
    title: String,
    subtitle: String? = null,
    showUpButton: Boolean = false,
    onUpButtonClicked: () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?
) {
    val expandedHeight =
        if (subtitle != null) 216.dp
        else 192.dp
    LargeTopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                subtitle?.let {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        },
        navigationIcon = {
            if (showUpButton) {
                IconButton(
                    onClick = onUpButtonClicked
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null
                    )
                }
            }
        },
        actions = actions,
        expandedHeight = expandedHeight,
        scrollBehavior = scrollBehavior
    )
}