package com.smscommands.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyListItem(
    title: String,
    content: String? = null,
    disabled: Boolean = false,
    onClick: (() -> Unit)? = null,
    divider: Boolean = false,
    colors: ListItemColors = ListItemDefaults.colors(),
    largeText: Boolean = false,
    maxContentLines: Int = 2,
    onContentLayout: ((TextLayoutResult) -> Unit)? = null,
    action: @Composable (() -> Unit)? = null
) {
    val clickable = !disabled && onClick != null

    Box(
        modifier = Modifier
            .clickable(enabled = clickable, onClick = { onClick?.invoke() })
            .alpha(if (disabled) 0.5f else 1f)
            .padding(vertical = 1.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(ListItemDefaults.containerColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = title,
                    style =
                        if (largeText) MaterialTheme.typography.titleLargeEmphasized
                        else MaterialTheme.typography.titleMediumEmphasized,
                    color =
                        if (!disabled) colors.headlineColor
                        else colors.disabledHeadlineColor,
                    maxLines = 1,
                    overflow = Ellipsis
                )
                content?.let {
                    // TODO: sliding animation
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMediumEmphasized,
                        color = colors.supportingTextColor,
                        maxLines = maxContentLines,
                        overflow = Ellipsis,
                        onTextLayout = onContentLayout
                    )
                }
            }

            if (divider) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                    )

                    VerticalDivider(
                        color = MaterialTheme.colorScheme.outline,
                        thickness = 0.8.dp,
                        modifier = Modifier
                            .height(32.dp)
                            .padding(start = 8.dp, end = 16.dp)
                    )

                }
            }

            action?.let {
                action()
            }
        }
    }
}
