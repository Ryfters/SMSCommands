package com.smscommands.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyListItem(
    title: String,
    content: String? = null,
    disabled: Boolean = false,
    onClick: (() -> Unit)? = null,
    separator: Boolean = false,
    action: @Composable (() -> Unit)? = null
) {

    val clickable = !disabled && onClick != null

    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(enabled = clickable, onClick = { onClick?.invoke() })
            .alpha(if (disabled) 0.5f else 1f)
            .padding(vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                maxLines = 1,
                overflow = Ellipsis
            )
            content?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = Ellipsis
                )
            }
        }

        if (separator) {
            VerticalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 0.8.dp,
                modifier = Modifier
                    .height(24.dp)
            )
        }

        action?.let {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                action()
            }
        }
    }
}
