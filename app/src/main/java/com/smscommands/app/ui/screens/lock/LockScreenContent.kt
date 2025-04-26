package com.smscommands.app.ui.screens.lock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smscommands.app.R

@Composable
fun LockScreenContent(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .shadow(elevation = 4.dp)
            .clickable(false) {}
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.screen_lock_locked),
            style = MaterialTheme.typography.displayLarge
        )
        Button(onClick) {
            Text(text = stringResource(R.string.screen_lock_unlock))
        }
    }
}
