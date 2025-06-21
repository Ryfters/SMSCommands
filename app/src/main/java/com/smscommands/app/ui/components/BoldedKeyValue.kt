package com.smscommands.app.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.smscommands.app.R

@Composable
fun BoldedKeyValue(key: String, value: String, modifier: Modifier = Modifier) {
    Text(buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(key) }
        append(stringResource(R.string.common_colon))
        append(value)
    }, modifier = modifier)
}