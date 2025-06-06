package com.smscommands.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun MyList(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.clip(RoundedCornerShape(26.dp))
    ) {
        content()
    }
}